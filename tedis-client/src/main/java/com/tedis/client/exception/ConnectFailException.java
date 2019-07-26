package com.tedis.client.exception;

public class ConnectFailException extends RuntimeException {
    public ConnectFailException(String msg) {
        super(msg);
    }
}
