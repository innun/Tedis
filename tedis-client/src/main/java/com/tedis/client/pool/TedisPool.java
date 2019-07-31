package com.tedis.client.pool;

import com.tedis.api.Connection;
import com.tedis.client.TedisClient;
import com.tedis.client.TedisClientConfig;
import com.tedis.client.exception.ConnectFailException;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class TedisPool {

    private AtomicInteger activeConns;
    private AtomicInteger idleConns;
    private AtomicInteger totalConns;
    private final int coreConns;
    private final int maxConns;
    private TedisClient client;
    private ConcurrentLinkedDeque<Connection> pool;


    public TedisPool(TedisPoolConfig tedisPoolConfig, TedisClientConfig tedisConfig) {
        activeConns = new AtomicInteger(0);
        idleConns = new AtomicInteger(0);
        totalConns = new AtomicInteger(0);
        this.client = TedisClient.create(tedisConfig);
        this.coreConns = tedisPoolConfig.getCoreConns();
        this.maxConns = tedisPoolConfig.getMaxConns();
        pool = new ConcurrentLinkedDeque<>();
        initPool();
    }

    private void initPool() {
        for (int i = 0; i < coreConns; i++) {
            addConnection();
        }
    }

    private void addConnection() {
        pool.add(client.connect());
        idleConns.getAndIncrement();
        totalConns.getAndIncrement();
    }

    public Connection getConn() {
        if (idleConns.get() == 0) {
            if (totalConns.get() >= maxConns) {
                throw new ConnectFailException("Number of connection is exceeded");
            } else {
                addConnection();
            }
        }
        activeConns.getAndIncrement();
        idleConns.getAndDecrement();
        return pool.removeFirst();
    }

    public void returnConn(Connection conn) {
        if (activeConns.get() >= coreConns) {
            conn.close();
            totalConns.getAndDecrement();
        } else {
            pool.addLast(conn);
            idleConns.getAndIncrement();
        }
        activeConns.getAndDecrement();
    }


    public void close() {
        for (Connection conn : pool) {
            conn.close();
        }
        activeConns.getAndSet(0);
        idleConns.getAndSet(0);
        totalConns.getAndSet(0);
        client.close();
    }
}
