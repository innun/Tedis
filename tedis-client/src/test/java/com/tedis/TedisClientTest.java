package com.tedis;

import com.tedis.api.Tedis;
import com.tedis.protocol.Result;
import org.junit.jupiter.api.Test;

public class TedisClientTest {

    @Test
    public void pingTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.ping();
        tedis.ping("HELLO");
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
    public void subTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.subscribe("news.it");
    }

    @Test
    public void publishTest() {
        Tedis tedis = TedisClient.tedis();
        tedis.publish("news", "hello");
    }

    @Test
    public void futureTest() {
        Tedis tedis = TedisClient.tedis();
        Result a = (Result) tedis.ping().sync();
        Result b = (Result) tedis.set("a", "1").sync();
        Result r = (Result) tedis.set("b", "2").sync();
        System.out.println(a.getResult() + b.getResult() + r.getResult());
    }

}
