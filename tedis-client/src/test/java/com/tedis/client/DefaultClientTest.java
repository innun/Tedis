package com.tedis.client;

import com.tedis.client.connection.Connection;
import com.tedis.client.pool.ConnPool;
import com.tedis.protocol.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultClientTest {

    @Test
    public void clientTest() throws InterruptedException {

        ConnPool pool = ConnPool.pool();
        Connection<Result> conn1 = null;
        Connection<Result> conn2 = null;
        try {
            conn1 = pool.connection();
            conn2 = pool.connection();
        assertEquals(conn1.get("TEST").sync().getResult(), "nil");
//        assertEquals(conn2.get("TEST").sync().getResult(), "nil");
        assertEquals(conn1.set("TEST",  "1").sync().getResult(), "\"OK\"");
        assertEquals(conn1.get("TEST").sync().getResult(), "\"1\"");

        } finally {
            conn1.recycle();
            conn2.recycle();
        }
    }
}
