package com.tedis;

import com.tedis.api.Tedis;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.connection.Pipeline;
import com.tedis.client.connection.Subscription;
import com.tedis.client.connection.TraditionalConn;
import com.tedis.client.pool.ConnPool;
import com.tedis.client.proxy.TedisInvocationHandler;
import com.tedis.protocol.Results;
import com.tedis.tools.BloomFilter;
import com.tedis.tools.locks.TedisLock;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Scanner;

public class TedisClient implements Tedis {
    public static final int TRADITIONAL = 1;
    public static final int PIPELINE = 1 << 1;
    public static final int SUB = 1 << 2;

    private ConnPool pool;
    private int mode;
    private Pipeline p;
    private TraditionalConn c;
    private Subscription s;

    private TedisClient() {
        init();
    }

    private void init() {
        pool = ConnPool.pool();
        setMode(TedisClient.TRADITIONAL);
    }

    public static Tedis tedis() {
        TedisClient tedisClient = new TedisClient();
        return (Tedis) Proxy.newProxyInstance(
                TedisClient.class.getClassLoader(),
                TedisClient.class.getInterfaces(),
                new TedisInvocationHandler(tedisClient));
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
        if (mode == TedisClient.PIPELINE) {
            pool.recycle(c, s);
            p = pool.pipeline();
        }
        if (mode == TedisClient.TRADITIONAL) {
            pool.recycle(p, s);
            c = pool.traditionalConn();
        }
        if (mode == TedisClient.SUB) {
            pool.recycle(c, p);
            s = pool.subscription();
        }
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public TedisFuture<Results> submit() {
        if (mode != TedisClient.PIPELINE) {
            throw new RuntimeException("Mode is not pipeline");
        }
        return p.submit();
    }

    private boolean traditional() {
        return mode == TedisClient.TRADITIONAL;
    }

    @Override
    public TedisFuture ping(String... msg) {
        return traditional() ? c.ping(msg) : p.ping(msg);
    }

    @Override
    public TedisFuture auth(String pass) {
        return traditional() ? c.auth(pass) : p.auth(pass);
    }

    @Override
    public TedisFuture set(String key, String value, String... optional) {
        return traditional()
                ? c.set(key, value, optional)
                : p.set(key, value, optional);
    }

    @Override
    public TedisFuture get(String key) {
        return traditional() ? c.get(key) : p.get(key);
    }

    @Override
    public TedisFuture setnx(String key, String value) {
        return traditional() ? c.setnx(key, value) : p.setnx(key, value);
    }

    @Override
    public TedisFuture incr(String key) {
        return traditional() ? c.incr(key) : p.incr(key);
    }

    @Override
    public TedisFuture del(String... keys) {
        return traditional() ? c.del(keys) : p.del(keys);
    }

    @Override
    public TedisFuture hmset(String key, String... pairs) {
        return traditional() ? c.hmset(key, pairs) : p.hmset(key, pairs);
    }

    @Override
    public TedisFuture eval(String script, int numKeys, String... args) {
        return traditional()
                ? c.eval(script, numKeys, args)
                : p.eval(script, numKeys, args);
    }

    @Override
    public TedisFuture setbit(String key, long offset, int value) {
        return traditional()
                ? c.setbit(key, offset, value)
                : p.setbit(key, offset, value);
    }

    @Override
    public TedisFuture getbit(String key, long offset) {
        return traditional() ? c.getbit(key, offset) : p.getbit(key, offset);
    }

    @Override
    public TedisFuture ttl(String key) {
        return traditional() ? c.ttl(key) : p.ttl(key);
    }

    @Override
    public void close() {
        if (c != null) {
            c.close();
        }
        if (p != null) {
            p.close();
        }
        pool.close();
    }

    //***************************tools**************************
    @Override
    public TedisLock newLock() {
        return newLock(3600);
    }

    @Override
    public TedisLock newLock(int expireTime) {
        return new TedisLock(c, expireTime);
    }

    @Override
    public BloomFilter newBloomFilter(long insertions, double falseProbability) {
        setMode(TedisClient.PIPELINE);
        return new BloomFilter(insertions, falseProbability, p);
    }


    //==============================SUB/PUB=================================
    @Override
    public TedisFuture subscribe(String... channels) {
        setMode(TedisClient.SUB);
        TedisFuture f = s.subscribe(channels);
        dealInput();
        return f;
    }

    @Override
    public TedisFuture psubscribe(String... patterns) {
        setMode(TedisClient.SUB);
        TedisFuture f = s.psubscribe(patterns);
        dealInput();
        return f;
    }

    private void dealInput() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String input = in.nextLine();
            String[] parts = input.split(" ");
            if (parts.length > 1) {
                String[] args = Arrays.copyOfRange(parts, 1, parts.length - 1);
                if (input.startsWith("unsubscribe")) {
                    unsubscribe(args);
                } else if (input.startsWith("punsubscribe")) {
                    punsubscribe(args);
                } else if (input.startsWith("subscribe")) {
                    subscribe(args);
                } else if (input.startsWith("psubscribe")) {
                    psubscribe(args);
                }
            }
        }
    }

    @Override
    public TedisFuture unsubscribe(String... channels) {
        TedisFuture f = s.unsubscribe(channels);
        setMode(TedisClient.TRADITIONAL);
        return f;
    }

    @Override
    public TedisFuture punsubscribe(String... patterns) {
        setMode(TedisClient.TRADITIONAL);
        return s.unsubscribe(patterns);
    }

    @Override
    public TedisFuture publish(String channel, String msg) {
        return traditional() ? c.publish(channel, msg) : p.publish(channel, msg);
    }

    @Override
    public TedisFuture pubsub(String subcommand, String... args) {
        return traditional() ? c.pubsub(subcommand, args) : p.pubsub(subcommand, args);
    }

}
