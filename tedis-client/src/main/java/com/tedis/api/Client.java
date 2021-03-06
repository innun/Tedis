package com.tedis.api;

import com.tedis.exception.ConnectFailException;
import io.netty.channel.Channel;

public interface Client {

    Channel connect() throws InterruptedException, ConnectFailException;

    void close();
}
