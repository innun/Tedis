package com.tedis.connection;

import com.tedis.common.Cmd;
import com.tedis.common.TedisFuture;
import com.tedis.pool.ConnPool;
import com.tedis.protocol.Command;
import com.tedis.protocol.Request;
import com.tedis.protocol.Result;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class Subscription extends AbstractBaseConn<Result> implements SubCmd {
    private static final AttributeKey<TedisFuture<Result>> FUTURE_KEY = AttributeKey.valueOf("future");

    public Subscription(ConnPool pool, Channel channel) {
        super(pool, channel);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public TedisFuture<Result> send(Cmd cmd, String... args) {
        TedisFuture<Result> future = new TedisFuture<>();
        Attribute<TedisFuture<Result>> attr = channel.attr(FUTURE_KEY);
        if (attr.get() != null) {
            attr.get().sync();
            attr.set(future);
        } else {
            attr.set(future);
        }
        channel.attr(RESULT_KEY).set(SUBSCRIBE_RESULT);
        Command command = generateCmd(cmd, args);
        try {
            channel.writeAndFlush(new Request<>(command)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
