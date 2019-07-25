package com.tedis.client;

import com.tedis.client.api.Connection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TedisClientTest {

    @Test
    public void clientTest() throws InterruptedException {
        TedisConfig conf = new TedisConfig("47.103.2.229", 6379);
        TedisClient client = TedisClient.create(conf);
        Connection conn = client.connect();
        assertEquals(conn.set("TEST", "1"), "\"OK\"");
        assertEquals(conn.get("TEST"), "\"1\"");
    }
}
