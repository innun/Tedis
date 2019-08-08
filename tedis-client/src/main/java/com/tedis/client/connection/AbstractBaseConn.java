package com.tedis.client.connection;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.ConnPool;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public abstract class AbstractBaseConn<T> implements Connection<T> {
    ConnPool pool;
    Channel channel;
    public static final AttributeKey<Integer> RESULT_KEY = AttributeKey.valueOf("result_key");
    static final int TRADITIONAL_CONN_RESULT = -1;
    static final int SUBSCRIBE_RESULT = -2;

    public AbstractBaseConn(ConnPool pool, Channel channel) {
        this.pool = pool;
        this.channel = channel;
    }

    abstract TedisFuture<T> send(Cmd cmd, String... args);

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void recycle() {
        pool.recycle(this);
    }

    @Override
    public Channel channel() {
        return channel;
    }
}
