package com.tedis.client;

public interface Client {
    Connection connect() throws InterruptedException;
}
