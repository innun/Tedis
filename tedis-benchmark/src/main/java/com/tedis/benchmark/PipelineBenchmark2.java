package com.tedis.benchmark;


import com.tedis.TedisClient;
import com.tedis.api.Tedis;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.redisson.Redisson;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 3)
@Fork(1)
@State(Scope.Thread)
public class PipelineBenchmark2 {
    Pipeline p;
    Tedis tedis;
    Config config;
    RedissonClient r;
    RBatch batch;

    @Setup
    public void setup() {
        Jedis jedis = new Jedis("47.103.2.229");
        jedis.auth("980608");
        p = jedis.pipelined();
        tedis = new TedisClient();
        tedis.setMode(TedisClient.PIPELINE);
        config = new Config();
        config.useSingleServer()
                .setAddress("redis://47.103.2.229:6379")
                .setPassword("980608");
        r = Redisson.create(config);
        batch = r.createBatch();
    }

//    @Benchmark
//    public void jedis() {
//        for (int i = 0; i < 1000; i++) {
//            p.set("key" + i, i + "");
//        }
//        p.sync();
//    }
//
//    @Benchmark
//    public void artis() {
//        for (int i = 0; i < 1000; i++) {
//            tedis.set("key" + i, i + "");
//        }
//        tedis.submit().sync();
//    }

    @Benchmark
    public void redisson() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 1000; i++) {
            r.getBucket("a").set("111");
        }
    }

    @TearDown
    public void teardown() {
        tedis.close();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PipelineBenchmark2.class.getSimpleName())
                .build();
        Runner t = new Runner(opt);
        t.run();
    }
}
