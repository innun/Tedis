package com.tedis.client.handler;

import com.tedis.protocol.RESPData;
import com.tedis.protocol.Response;
import com.tedis.protocol.codec.RESPDataParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = in.array();
        Response response = null;
        switch ((char) bytes[0]) {
            case RESPData.ARRAY_PREFIX: {
                String res = RESPDataParser.parseArray(bytes, 1).getResult();
                response = new Response(RESPData.ARRAY_TYPE, res);
                break;
            }
            case RESPData.INTEGER_PREFIX: {
                String res = RESPDataParser.parseInteger(bytes, 1).getResult();
                response = new Response(RESPData.INTEGER_TYPE, res);
                break;
            }
            case RESPData.BULK_STRING_PREFIX: {
                String res = RESPDataParser.parseBulkString(bytes, 1).getResult();
                response = new Response(RESPData.BULK_STRING_TYPE, res);
                break;
            }
            case RESPData.SIMPLE_STRING_PREFIX: {
                String res = RESPDataParser.parseSimpleString(bytes, 1).getResult();
                response = new Response(RESPData.SIMPLE_STRING_TYPE, res);
                break;
            }
            case RESPData.ERROR_PREFIX: {
                String res = RESPDataParser.parseError(bytes, 1).getResult();
                String[] error = res.split(" ");
                response = new Response(RESPData.ERROR_TYPE, error[0], error[1]);
            }
        }
        out.add(response);
    }
}
