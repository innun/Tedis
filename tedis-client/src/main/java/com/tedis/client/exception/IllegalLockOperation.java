package com.tedis.client.exception;

public class IllegalLockOperation extends RuntimeException {
    public IllegalLockOperation(String msg) {
        super(msg);
    }
}
