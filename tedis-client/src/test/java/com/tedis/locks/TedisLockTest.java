package com.tedis.locks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TedisLockTest {

    private int v1 = 0;
    private int v2 = 0;
    private static TedisLock lock;

    @BeforeAll
    public static void before() {
        lock = new TedisLock();
    }

    @Test
    public void testLock() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread t1 = new Thread(new UnsafeTask());
        Thread t2 = new Thread(new UnsafeTask());
        Thread t3 = new Thread(new SafeTask());
        Thread t4 = new Thread(new SafeTask());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        System.out.println(System.currentTimeMillis() - begin);
        assertNotEquals(v1, 2000);
        assertEquals(v2, 2000);
    }

    private void incr() {
        v1++;
    }

    private void safeIncr() {
        try {
            lock.lock();
            v2++;
        } finally {
            System.out.println(v2);
            lock.unlock();
        }

    }

    class UnsafeTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 500; i++) {
                incr();
            }
        }
    }

    class SafeTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 500; i++) {
                safeIncr();
            }
        }
    }

}