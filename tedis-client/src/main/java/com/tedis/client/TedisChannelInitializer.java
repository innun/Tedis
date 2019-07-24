package com.tedis.client;

import com.tedis.client.handler.RequestEncoder;
import com.tedis.client.handler.TedisChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class TedisChannelInitializer<T extends Channel> extends ChannelInitializer<T> {

    @Override
    protected void initChannel(T t) throws Exception {
        t.pipeline().addLast(new TedisChannelHandler())
        .addLast(new RequestEncoder());
    }
}
