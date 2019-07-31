package com.tedis.client;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.protocol.Command;
import com.tedis.protocol.Commands;
import com.tedis.protocol.Request;
import io.netty.channel.Channel;

public class Pipeline extends AbstractConnection {

    private Commands cmds;


    public Pipeline(Channel channel) {
        super(channel);
        cmds = new Commands();
    }

//    public static Pipeline pipeline(Channel channel) {
//        return new Pipeline(channel);
//    }

    @Override
    TedisFuture<String> execute(Cmd cmd, String... params) {
        Command c = generateCmd(cmd, params);
        cmds.add(c);
        return null;
    }

    public TedisFuture<String> submit() {
        Request<Commands> req = new Request<>(cmds);
        TedisFuture<String> future = new TedisFuture<>();
        channel.attr(KEY).set(future);
        channel.writeAndFlush(req);
        return future;
    }

}
