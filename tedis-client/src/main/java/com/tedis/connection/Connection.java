package com.tedis.connection;

import io.netty.channel.Channel;


public interface Connection<T> {

    void close();

    void recycle();

    Channel channel();

}
