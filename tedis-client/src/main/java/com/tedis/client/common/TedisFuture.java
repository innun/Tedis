package com.tedis.client.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TedisFuture <T> extends CompletableFuture<T> {
    public T sync() {
        try {
            return this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
