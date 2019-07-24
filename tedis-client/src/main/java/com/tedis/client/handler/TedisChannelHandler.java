package com.tedis.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class TedisChannelHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(TedisChannelHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info("Connected to {}", channel.remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {

    }

    @ Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable error) {

    }
}
