package com.tedis.locks;

import com.tedis.api.Connection;
import com.tedis.api.Lock;
import com.tedis.client.TedisClient;
import com.tedis.client.TedisConfig;
import com.tedis.client.exception.ConnectFailException;
import com.tedis.client.exception.IllegalLockOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TedisLock implements Lock {

    private static Logger log = LoggerFactory.getLogger(TedisLock.class);

    private static final String LOCK_KEY = "LOCK_KEY";
    private static Map<String, Connection> map = new ConcurrentHashMap<>();

    public TedisLock() {}

    @Override
    public void lock() {
        String threadName = Thread.currentThread().getName();
        Connection conn = null;
        if (map.containsKey(threadName)) {
            conn = map.get(threadName);
        } else {
            try {
                conn = TedisClient.create(TedisConfig.DEFAULT_CONFIG).connect();
                map.put(threadName, conn);
            } catch (ConnectFailException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (conn == null) {
            throw new ConnectFailException("lock init fail");
        }
        while (true) {
            String result = conn.setnx(LOCK_KEY, "value");
            if (result.equals("1")) {
                log.info(Thread.currentThread().getName() + " acquires lock success");
                break;
            }
            log.info(Thread.currentThread().getName() + " acquires lock fail");
        }
    }

    @Override
    public void unlock() {
        Connection conn = map.get(Thread.currentThread().getName());
        if (conn == null) {
            throw new IllegalLockOperation("current thread does not maintain a lock");
        }
        conn.del(LOCK_KEY);
    }
}
