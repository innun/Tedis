package com.tedis.api;

import com.tedis.client.exception.ConnectFailException;

public interface Client {
    Connection connect() throws InterruptedException, ConnectFailException;

    void close();
}
