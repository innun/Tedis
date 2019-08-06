package com.tedis;

import com.tedis.protocol.Result;
import org.junit.jupiter.api.Test;

public class TedisTest {

    @Test
    public void test() {
        Tedis tedis = new Tedis();
        tedis.setMode(Tedis.PIPELINE);
        tedis.get("a");
        tedis.set("a", "1");
        tedis.get("a");
        for (Result r : tedis.submit().sync()) {
            System.out.println(r.getResult());
        }
    }
}
