package com.tedis.tools.locks;

import com.tedis.Tedis;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TedisLockTest {

    private int v1 = 0;
    private int v2 = 0;
    private static TedisLock ClientA_Lock;
    private static TedisLock ClientB_Lock;
    private static Tedis tedis;


    @BeforeAll
    public static void before() {
        tedis = new Tedis();
        ClientA_Lock = tedis.newLock();
        ClientB_Lock = tedis.newLock();
    }

    @Test
    public void testLock() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread t1 = new Thread(new UnsafeTask());
        Thread t2 = new Thread(new UnsafeTask());
        Thread t3 = new Thread(new SafeTaskA());
        Thread t4 = new Thread(new SafeTaskB());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        System.out.println("cost:" + (System.currentTimeMillis() - begin) + "ms");
//        assertNotEquals(v1, 20000);
        assertEquals(v2, 20);
    }

    private void incr() {
        v1++;
    }

    private void safeIncrA() {
        try {
            ClientA_Lock.lock();
            v2++;
        } finally {
            ClientA_Lock.unlock();
        }

    }

    private void safeIncrB() {
        try {
            ClientB_Lock.lock();
            v2++;
        } finally {
            ClientB_Lock.unlock();
        }

    }

    class UnsafeTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                incr();
            }
        }
    }

    class SafeTaskA implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                safeIncrA();
            }
        }
    }

    class SafeTaskB implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                safeIncrB();
            }
        }
    }

    @Test
    public void updateExTimeTest() throws InterruptedException {
        TedisLock lock1 = tedis.newLock();
        TedisLock lock2 = tedis.newLock();
        Thread thread1 = new Thread(() -> {
            try {
                lock1.lock();
                try {
                    System.out.println("Thread1 lock");
                    Thread.sleep(18000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                System.out.println("Thread1 unlock");
                lock1.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                lock2.lock();
                try {
                    System.out.println("Thread2 lock");
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                System.out.println("Thread2 unlock");
                lock2.unlock();
            }
        });
        thread1.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        thread1.join();
        thread2.join();
    }

}