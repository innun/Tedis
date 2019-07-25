package com.tedis.client.api;

public interface Client {
    Connection connect() throws InterruptedException;
}
