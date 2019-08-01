package com.tedis.client;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.protocol.*;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TedisConnection extends AbstractConnection<Result> {
    private static Logger log = LoggerFactory.getLogger(TedisConnection.class);
    public static final AttributeKey<TedisFuture<Result>> FUTURE_KEY = AttributeKey.valueOf("future");


    public TedisConnection(Channel channel) {
        super(channel);
    }

    @Override
    TedisFuture<Result> execute(Cmd cmd, String... params) {
        TedisFuture<Result> future = new TedisFuture<>();
        Request<Command> req = new Request<>(generateCmd(cmd, params));
        channel.attr(FUTURE_KEY).set(future);
        channel.attr(RESULT_NUM_KEY).set(1);
        try {
            channel.writeAndFlush(req).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}
