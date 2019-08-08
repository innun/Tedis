package com.tedis.client.connection;

import io.netty.channel.Channel;


public interface Connection<T> {

    void close();

    void recycle();

    Channel channel();

}
