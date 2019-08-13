package benchmark;

import com.tedis.connection.Pipeline;
import com.tedis.connection.TraditionalConn;
import com.tedis.pool.ConnPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class PipelineBenchmark {
    static TraditionalConn conn1;
    static ConnPool pool;
    static Pipeline p;

    @BeforeAll
    public static void before() {
        pool = ConnPool.pool();
    }

    private long reqres(int a) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < a; i++) {
            conn1.set("a" + i, "" + i).sync();
        }
        return System.currentTimeMillis() - begin;
    }

    private long pipe(int a) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < a; i++) {
            p.set("a" + i, "" + i);
        }
        p.submit().sync();
        return System.currentTimeMillis() - begin;
    }

    private void round(int a, int b) {
        conn1 = pool.traditionalConn();
        long beginr = 0;
        for (int i = 0; i < b; i++) {
            System.out.println("reqres round " + i);
            beginr += reqres(a);
        }
        p = pool.pipeline();
        long beginp = 0;
        for (int i = 0; i < b; i++) {
            System.out.println("pipe round " + i);
            beginp += pipe(a);
        }
        try (FileOutputStream o = new FileOutputStream("benchmark_result")) {
            o.write((a + " " + b + "\n").getBytes());
            o.write((1.0 * beginr / b + " " + 1.0 * beginp / b + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(a + " command(s) call and " + b + " round(s), average:");
        System.out.println("Request/Response: " + 1.0 * beginr / b + "ms");
        System.out.println("Pipeline: " + 1.0 * beginp / b + "ms");
    }

    @Test
    public void benchmark() {
//        round(1, 1000);
        round(100, 100);
        round(1000, 100);
     }
}
