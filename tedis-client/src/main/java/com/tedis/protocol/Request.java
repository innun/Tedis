package com.tedis.protocol;


public class Request<T> {
    private T payload;

    public Request(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return payload.toString();
    }
}
