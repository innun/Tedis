package com.tedis.exception;

public class ConnectFailException extends RuntimeException {
    public ConnectFailException(String msg) {
        super(msg);
    }
}
