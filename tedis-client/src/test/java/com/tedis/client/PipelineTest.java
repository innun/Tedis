package com.tedis.client;

import com.tedis.client.connection.Pipeline;
import com.tedis.client.pool.ConnPool;
import com.tedis.protocol.Result;
import com.tedis.protocol.Results;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PipelineTest {

    static ConnPool pool;
    static Pipeline p;

    @BeforeAll
    public static void before() {
        pool = ConnPool.pool();
        p = pool.pipeline();
    }

    @Test
    void submit() {
        try {
            p.set("a", "1");
            p.set("b", "2");
            p.set("c", "3");
            Results results = p.submit().sync();
            for (Result r : results.getResults()) {
                assertEquals(r.getResult(), "\"OK\"");
            }
            p.get("a");
            p.get("b");
            p.get("c");
            results = p.submit().sync();
            int i = 1;
            for (Result r : results.getResults()) {
                assertEquals(r.getResult(), "\"" + i + "\"");
                i++;
            }
        } finally {
            p.recycle();
        }
    }
}