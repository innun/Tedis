package com.tedis.connection;

import com.tedis.common.Cmd;
import com.tedis.common.TedisFuture;
import com.tedis.pool.ConnPool;
import com.tedis.protocol.Command;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractBaseConn<T> implements Connection<T> {
    ConnPool pool;
    Channel channel;
    public static final AttributeKey<Integer> RESULT_KEY = AttributeKey.valueOf("result_key");
    public static final int TRADITIONAL_CONN_RESULT = -1;
    public static final int SUBSCRIBE_RESULT = -2;

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

    Command generateCmd(Cmd cmd, String... params) {
        String cmdName = cmd.getCmd();
        List<String> parts = new ArrayList<>();
        parts.add(cmdName);
        parts.addAll(Arrays.asList(params));
        return new Command(parts);
    }
}
