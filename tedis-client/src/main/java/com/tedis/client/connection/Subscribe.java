package com.tedis.client.connection;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.ConnPool;
import com.tedis.protocol.Result;
import io.netty.channel.Channel;

public class Subscribe extends AbstractBaseConn<Result> implements SubCmd {

    public Subscribe(ConnPool pool, Channel channel) {
        super(pool, channel);
    }

    @Override
    public TedisFuture<Result> send(Cmd cmd, String... args) {
        // TODO
        return null;
    }

    @Override
    public TedisFuture subscribe(String... channels) {
        return send(Cmd.SUBSCRIBE, channels);
    }

    @Override
    public TedisFuture unsubscribe(String... channels) {
        return send(Cmd.UNSUBSCRIBE, channels);
    }

    @Override
    public TedisFuture psubscribe(String... patterns) {
        return send(Cmd.PSUBSCRIBE, patterns);
    }

    @Override
    public TedisFuture punsubscribe(String... patterns) {
        return send(Cmd.PUNSUBSCRIBE, patterns);
    }
}
