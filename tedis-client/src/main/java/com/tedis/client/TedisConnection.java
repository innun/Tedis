package com.tedis.client;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.protocol.Command;
import com.tedis.protocol.Request;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TedisConnection extends AbstractConnection {
    private static Logger log = LoggerFactory.getLogger(TedisConnection.class);

    private TedisFuture<String> future;

    public TedisConnection(Channel channel) {
        super(channel);
    }

    @Override
    TedisFuture<String> execute(Cmd cmd, String... params) {
        future = new TedisFuture<>();
        Request<Command> req = new Request<>(generateCmd(cmd, params));
        channel.attr(KEY).set(future);
        channel.writeAndFlush(req);
        return future;
    }
}
