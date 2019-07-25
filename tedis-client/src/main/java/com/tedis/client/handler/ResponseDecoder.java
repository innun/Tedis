package com.tedis.client.handler;

import com.tedis.protocol.RESPData;
import com.tedis.protocol.Response;
import com.tedis.protocol.codec.ParseInfo;
import com.tedis.protocol.codec.RESPDataParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResponseDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(ResponseDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info("Connection created {}", channel);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(in);
        Response response = null;
        ParseInfo res = null;
        switch ((char) bytes[0]) {
            case RESPData.ARRAY_PREFIX: {
                res = RESPDataParser.parseArray(bytes, 1);
                response = new Response(RESPData.ARRAY_TYPE, res.getResult());
                break;
            }
            case RESPData.INTEGER_PREFIX: {
                res = RESPDataParser.parseInteger(bytes, 1);
                response = new Response(RESPData.INTEGER_TYPE, res.getResult());
                break;
            }
            case RESPData.BULK_STRING_PREFIX: {
                res = RESPDataParser.parseBulkString(bytes, 1);
                response = new Response(RESPData.BULK_STRING_TYPE, res.getResult());
                break;
            }
            case RESPData.SIMPLE_STRING_PREFIX: {
                res = RESPDataParser.parseSimpleString(bytes, 1);
                response = new Response(RESPData.SIMPLE_STRING_TYPE, res.getResult());
                break;
            }
            case RESPData.ERROR_PREFIX: {
                res = RESPDataParser.parseError(bytes, 1);
                String[] error = res.getResult().split(" ");
                response = new Response(RESPData.ERROR_TYPE, error[0], error[1]);
            }
        }
        assert response != null;
        if (response.getResult() == null) {
            return;
        }
        logDecoder(in, res.getRawLen(), response);
        // 消费ByteBuf
        int curRdIdx = in.readerIndex();
        in.readerIndex(curRdIdx + res.getRawLen());
        out.add(response);
    }

    @SuppressWarnings("Duplicates")
    private void logDecoder(ByteBuf buf, int len, Response res) {
        byte[] bytes = ByteBufUtil.getBytes(buf, buf.readerIndex(), len);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b == (byte) '\r') {
                sb.append("\\r");
            } else if (b == (byte) '\n') {
                sb.append("\\n");
            } else {
                sb.append((char) b);
            }
        }
        log.info("Response arrived => {} ({})", sb.toString(), res);
    }
}
