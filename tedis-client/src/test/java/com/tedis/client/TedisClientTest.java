package com.tedis.client;

import com.tedis.api.Connection;
import com.tedis.client.pool.TedisPool;
import com.tedis.client.pool.TedisPoolConfig;
import com.tedis.protocol.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TedisClientTest {

    @Test
    public void clientTest() throws InterruptedException {
        TedisClientConfig tedisClientConfig = TedisClientConfig.DEFAULT_CONFIG;
        TedisPoolConfig tedisPoolConfig = TedisPoolConfig.DEFAULT_TEDIS_POOL_CONFIG;
        TedisPool pool = new TedisPool(tedisPoolConfig, tedisClientConfig);
        Connection<Result> conn1 = null;
        Connection<Result> conn2 = null;
        try {
            conn1 = pool.connection();
            conn2 = pool.connection();
        assertEquals(conn1.get("TES").sync().getResult(), "nil");
        assertEquals(conn2.get("TES").sync().getResult(), "nil");

        } finally {
            conn1.returnToPool(pool);
            conn2.returnToPool(pool);
        }
    }
}
