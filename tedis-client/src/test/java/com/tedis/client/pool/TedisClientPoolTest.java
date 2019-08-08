package com.tedis.client.pool;

import com.tedis.client.connection.Connection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TedisClientPoolTest {
    private static ConnPool pool;



    @Test
    public void connectionTest() {
        pool = ConnPool.pool();

        Connection conn1 = null;
        Connection conn2 = null;
        Connection conn3 = null;
        Connection conn4 = null;
        Connection conn5 = null;
        try {
            conn1 = pool.connection();
            conn2 = pool.connection();
            conn3 = pool.connection();
            conn4 = pool.connection();
            assertEquals(pool.getActiveConns().get(), 4);
            assertEquals(pool.getIdleConns().get(), 0);
            assertEquals(pool.getTotalConns().get(), 4);
            conn5 = pool.connection();
            assertEquals(pool.getActiveConns().get(), 5);
            assertEquals(pool.getIdleConns().get(), 0);
            assertEquals(pool.getTotalConns().get(), 5);
        } finally {
            pool.recycle(conn1);
            pool.recycle(conn2);
            pool.recycle(conn3);
            pool.recycle(conn4);
            pool.recycle(conn5);
            assertEquals(pool.getActiveConns().get(), 0);
            assertEquals(pool.getIdleConns().get(), 4);
            assertEquals(pool.getTotalConns().get(), 4);
        }
    }


}