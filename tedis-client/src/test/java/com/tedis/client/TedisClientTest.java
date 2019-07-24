package com.tedis.client;

import org.junit.jupiter.api.Test;

public class TedisClientTest {

    @Test
    public void clientTest() throws InterruptedException {
        TedisConfig conf = new TedisConfig("47.103.2.229", 6379);
        TedisClient client = TedisClient.create(conf);
        Connection conn = client.connect();
    }
}
