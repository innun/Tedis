package com.tedis.client;

import com.tedis.api.Connection;
import com.tedis.client.exception.ConnectFailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TedisClientTest {

    @Test
    public void clientTest() throws InterruptedException {
        TedisConfig conf = TedisConfig.build()
                .host("47.103.2.229")
                .port(6379)
                .password("98060");

        TedisClient client = TedisClient.create(conf);
        try {
            Connection conn = client.connect();
//        assertEquals(conn.set("TEST", "1", "EX", "1"), "\"OK\"");
            assertEquals(conn.get("TES"), "nil");
//        conn.incr("T");
//        assertEquals(conn.setnx("HELLO", "2"), "1");

        } catch (ConnectFailException e) {
            e.printStackTrace();
            client.close();
        }
    }
}
