package com.tedis.exception;

public class IllegalLockOperation extends RuntimeException {
    public IllegalLockOperation(String msg) {
        super(msg);
    }
}
