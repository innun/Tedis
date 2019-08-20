package com.tedis.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@Threads(4)
@State(Scope.Benchmark)
public class DequeBenchmark {
    private ConcurrentLinkedDeque<Integer> cld = new ConcurrentLinkedDeque<>();
    private LinkedBlockingDeque<Integer> lbd = new LinkedBlockingDeque<>();

    @Benchmark
    public void l() {
        lbd.offer(1);
        lbd.poll();
    }

    @Benchmark
    public void c() {
        cld.offer(1);
        cld.poll();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DequeBenchmark.class.getSimpleName())
                .build();
        Runner t = new Runner(opt);
        t.run();
    }
}
