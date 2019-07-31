package com.tedis.client.handler;

import com.tedis.client.TedisConnection;
import com.tedis.client.common.TedisFuture;
import com.tedis.protocol.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class TedisChannelInBoundHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(TedisChannelInBoundHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        if (obj instanceof Response) {
            Response response = (Response) obj;
            Channel channel = ctx.channel();
            TedisFuture<String> future = channel.attr(TedisConnection.KEY).get();
            future.complete(response.getResult());
        }
    }

    @ Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable error) {

    }
}
