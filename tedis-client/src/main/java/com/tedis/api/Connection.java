package com.tedis.api;

import io.netty.channel.Channel;


public interface Connection<T> extends Command<T> {

    void close();

    void returnToPool();

    Channel channel();

}
