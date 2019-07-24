package com.tedis.client.handler;

import com.tedis.protocol.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestEncoder extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) {
        out.writeBytes(msg.getPayload());
    }
}
