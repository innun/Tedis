package com.tedis.api;

import com.tedis.client.pool.TedisPool;
import io.netty.channel.Channel;


public interface Connection<T> extends Command<T> {

    void close();

    void returnToPool(TedisPool pool);

    Channel channel();

}
