package com.tedis.pool;

import com.tedis.connection.Connection;
import com.tedis.connection.Pipeline;
import com.tedis.connection.Subscription;
import com.tedis.connection.TraditionalConn;
import com.tedis.exception.ConnectFailException;
import com.tedis.config.TedisClientConfig;
import com.tedis.config.TedisPoolConfig;
import com.tedis.config.TedisProperties;
import com.tedis.protocol.Results;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnPool {
    private static Logger log = LoggerFactory.getLogger(ConnPool.class);
    private AtomicInteger activeConns;
    private AtomicInteger idleConns;
    private AtomicInteger totalConns;
    // com.tedis.pool.coreConns
    private final int coreConns;
    // com.tedis.pool.maxConns
    private final int maxConns;
    private DefaultClient client;
    private ConcurrentLinkedDeque<Channel> pool;
    private static volatile ConnPool instance;

    private ConnPool(TedisPoolConfig tedisPoolConfig, TedisClientConfig tedisConfig) {
        activeConns = new AtomicInteger(0);
        idleConns = new AtomicInteger(0);
        totalConns = new AtomicInteger(0);
        this.client = DefaultClient.create(tedisConfig);
        this.coreConns = tedisPoolConfig.getCoreConns();
        this.maxConns = tedisPoolConfig.getMaxConns();
        pool = new ConcurrentLinkedDeque<>();
        initPool();
    }

    private void initPool() {
        for (int i = 0; i < coreConns; i++) {
            addChannel();
        }
    }

    private void addChannel() {
        pool.add(client.connect());
        idleConns.getAndIncrement();
        totalConns.getAndIncrement();
    }

    public static ConnPool pool() {
        if (instance == null) {
            synchronized (ConnPool.class) {
                if (instance == null) {
                    HashMap<String, String> properties = new TedisProperties().getProperties();
                    TedisPoolConfig poolConf = TedisPoolConfig.build();
                    TedisClientConfig clientConf = TedisClientConfig.build();
                    Set<String> keySet = properties.keySet();
                    try {
                        for (String key : keySet) {
                            if (key.startsWith(TedisPoolConfig.PROP_PREFIX)) {
                                String name = key.substring(TedisPoolConfig.PROP_PREFIX.length());
                                Method method = TedisPoolConfig.class.getDeclaredMethod(name, String.class);
                                method.invoke(poolConf, properties.get(key));
                            } else if (key.startsWith(TedisClientConfig.PROP_PREFIX)) {
                                String name = key.substring(TedisClientConfig.PROP_PREFIX.length());
                                Method method = TedisClientConfig.class.getDeclaredMethod(name, String.class);
                                method.invoke(clientConf, properties.get(key));
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    instance = new ConnPool(poolConf, clientConf);
                }
            }
        }
        return instance;
    }

    public TraditionalConn traditionalConn() {
        checkIdle();
        TraditionalConn conn = new TraditionalConn(this, pool.removeFirst());
        idleConns.getAndDecrement();
        activeConns.getAndIncrement();
        if (!conn.auth(client.getPassword()).sync().getResult().equals("\"OK\"")) {
            throw new ConnectFailException("invalid password");
        }
        return conn;
    }

    public Pipeline pipeline() {
        checkIdle();
        Pipeline p = new Pipeline(this, getPooledChannel());
        p.auth(client.getPassword());
        Results r = p.submit().sync();
        if (!r.getResults().get(0).getResult().equals("\"OK\"")) {
            throw new ConnectFailException("invalid password");
        }
        return p;
    }

    public Subscription subscription() {
        checkIdle();
        TraditionalConn conn = traditionalConn();
        if (!conn.auth(client.getPassword()).sync().getResult().equals("\"OK\"")) {
            throw new ConnectFailException("invalid password");
        }
        Channel channel = conn.channel();
        return new Subscription(this, channel);
    }

    private Channel getPooledChannel() {
        Channel c = pool.removeFirst();
        if (!validate(c)) {
            c = client.connect();
        }
        idleConns.getAndDecrement();
        activeConns.getAndIncrement();
        return c;
    }

    private void checkIdle() {
        if (idleConns.get() == 0) {
            if (totalConns.get() >= maxConns) {
                throw new ConnectFailException("Number of connection is exceeded");
            } else {
                addChannel();
            }
        }
    }

    public void recycle(Connection... conn) {
        for (Connection c : conn) {
            if (c != null) {
                if (totalConns.get() > coreConns) {
                    c.close();
                    activeConns.getAndDecrement();
                    totalConns.getAndDecrement();
                } else {
                    if (validate(c.channel())) {
                        pool.addLast(c.channel());
                        idleConns.getAndIncrement();
                        activeConns.getAndDecrement();
                    } else {
                        totalConns.getAndDecrement();
                        log.info("channel {} invalid, add a new one to pool.", c.channel());
                        addChannel();
                        activeConns.getAndDecrement();
                    }
                }
            }
        }
    }

    private boolean validate(Channel channel) {
        if (!channel.isActive()) {
            return false;
        }
        TraditionalConn conn = new TraditionalConn(this, channel);
        String result = conn.ping().sync().getResult();
        if (!result.equals("\"PONG\"")) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }


    public void close() {
        for (Channel chan : pool) {
            chan.close();
        }
        activeConns.getAndSet(0);
        idleConns.getAndSet(0);
        totalConns.getAndSet(0);
        client.close();
    }

    public AtomicInteger getActiveConns() {
        return activeConns;
    }

    public AtomicInteger getIdleConns() {
        return idleConns;
    }

    public AtomicInteger getTotalConns() {
        return totalConns;
    }
}
