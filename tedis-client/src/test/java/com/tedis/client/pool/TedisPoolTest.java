package com.tedis.client.pool;

import com.tedis.api.Connection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TedisPoolTest {
    private static TedisPool pool;



    @Test
    public void connectionTest() {
        pool = TedisPool.pool();

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
            pool.receive(conn1);
            pool.receive(conn2);
            pool.receive(conn3);
            pool.receive(conn4);
            pool.receive(conn5);
            assertEquals(pool.getActiveConns().get(), 0);
            assertEquals(pool.getIdleConns().get(), 4);
            assertEquals(pool.getTotalConns().get(), 4);
        }
    }


}