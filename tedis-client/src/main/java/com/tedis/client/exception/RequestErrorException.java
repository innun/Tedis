package com.tedis.client.exception;

public class RequestErrorException extends Exception {

    public RequestErrorException() {
        super("Request generate fail!");
    }

    public RequestErrorException(String msg) {
        super(msg);
    }
}
