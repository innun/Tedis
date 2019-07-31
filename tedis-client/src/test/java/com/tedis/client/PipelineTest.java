package com.tedis.client;

import com.tedis.api.Connection;
import com.tedis.client.pool.TedisPool;
import com.tedis.client.pool.TedisPoolConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PipelineTest {

    TedisPool pool;
    Connection conn;

    @BeforeAll
    public void before() {
        pool = new TedisPool(TedisPoolConfig.DEFAULT_TEDIS_POOL_CONFIG,
                TedisClientConfig.DEFAULT_CONFIG);
        conn = pool.getConn();
    }

    @Test
    void submit() {
        try {
            Pipeline p = new Pipeline(conn.channel());
            p.set("a", "1");
            p.set("b", "2");
            p.set("c", "3");
            p.submit().sync();

        } finally {
            conn.returnToPool(pool);
        }
    }
}