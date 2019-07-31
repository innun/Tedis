package benchmark;

import com.tedis.api.Connection;
import com.tedis.client.Pipeline;
import com.tedis.client.TedisClientConfig;
import com.tedis.client.pool.TedisPool;
import com.tedis.client.pool.TedisPoolConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PipelineBenchmark {
    static Connection conn1;
    static Connection conn2;
    static TedisPool pool;
    static Pipeline p;

    @BeforeAll
    public static void before() {
        pool = new TedisPool(
                TedisPoolConfig.DEFAULT_TEDIS_POOL_CONFIG,
                TedisClientConfig.DEFAULT_CONFIG);
        conn1 = pool.getConn();
        conn2 = pool.getConn();
        p = new Pipeline(conn2.channel());
    }
    @Test
    public void benchmark() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            conn1.set("a" + i, "" + i).sync();
        }
        System.out.println("Request/Response: " + (System.currentTimeMillis() - begin) + "ms");
        begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            p.set("a" + i, "" + i);
        }
        p.submit().sync();
        System.out.println("Pipeline: " + (System.currentTimeMillis() - begin) + "ms");
    }
}
