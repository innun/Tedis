package benchmark;

import com.tedis.client.connection.Pipeline;
import com.tedis.client.connection.TraditionalConn;
import com.tedis.client.pool.ConnPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PipelineBenchmark {
    static TraditionalConn conn1;
    static ConnPool pool;
    static Pipeline p;

    @BeforeAll
    public static void before() {
        pool = ConnPool.pool();
        conn1 = pool.traditionalConn();
        p = pool.pipeline();
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
