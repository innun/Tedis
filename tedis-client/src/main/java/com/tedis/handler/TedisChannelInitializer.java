package com.tedis.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class TedisChannelInitializer<T extends Channel> extends ChannelInitializer<T> {

    @Override
    protected void initChannel(T t) {
        t.pipeline().addLast(new ResponseDecoder())
                .addLast(new TedisChannelInBoundHandler())
                .addLast(new RequestEncoder());
    }
}
