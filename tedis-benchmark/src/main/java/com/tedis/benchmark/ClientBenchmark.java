package com.tedis.benchmark;


import com.tedis.TedisClient;
import com.tedis.api.Tedis;
import com.tedis.protocol.Result;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(iterations = 30, time = 30)
@Fork(1)
@State(Scope.Thread)
public class ClientBenchmark {
    Jedis jedis;
    Tedis tedis;

    @Setup
    public void setup() {
        jedis = new Jedis("47.103.2.229");
        jedis.auth("980608");
        tedis = TedisClient.tedis();
    }

    @Benchmark
    public void jedis() {
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
    }

    @Benchmark
    public void artis() {
        tedis.set("foo", "bar");
        String value = ((Result) (tedis.get("foo").sync())).getResult();
    }

    @TearDown
    public void teardown() {
        tedis.close();
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ClientBenchmark.class.getSimpleName())
                .build();
        Runner t = new Runner(opt);
        t.run();
    }
}
