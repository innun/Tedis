package com.tedis.common;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureTest {



    public Future<Integer> search() throws ExecutionException, InterruptedException {
        FutureTask<Integer> f = new FutureTask<>(() -> {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        });
        new Thread(f).start();
        return f;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(new FutureTest().search().get());
    }
}
