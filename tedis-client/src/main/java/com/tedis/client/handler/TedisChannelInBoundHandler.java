package com.tedis.client.handler;

import com.tedis.client.connection.Pipeline;
import com.tedis.client.connection.TraditionalConn;
import com.tedis.client.common.TedisFuture;
import com.tedis.protocol.Result;
import com.tedis.protocol.Results;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TedisChannelInBoundHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(TedisChannelInBoundHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info("Connection created {}", channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        Channel channel = ctx.channel();
        if (obj instanceof Result) {
            TedisFuture<Result> future = channel.attr(TraditionalConn.FUTURE_KEY).get();
            future.complete((Result) obj);
        }
        if (obj instanceof Results) {
            TedisFuture<Results> future = channel.attr(Pipeline.FUTURE_KEY).get();
            future.complete((Results) obj);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable error) {
        ctx.fireExceptionCaught(error);
    }
}
