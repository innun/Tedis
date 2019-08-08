package com.tedis;

import com.tedis.api.Tedis;
import com.tedis.protocol.Result;
import org.junit.jupiter.api.Test;

public class TedisClientTest {

    @Test
    public void pingTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.ping();
        tedis.ping();
    }

    @Test
    public void pipelineTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.setMode(TedisClient.PIPELINE);
        tedis.get("a");
        tedis.set("a", "1");
        tedis.get("a");
        for (Result r : tedis.submit().sync()) {
            System.out.println(r.getResult());
        }
    }

    @Test
    public void proxyTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.subscribe("news.it");
        tedis.close();
    }

}
