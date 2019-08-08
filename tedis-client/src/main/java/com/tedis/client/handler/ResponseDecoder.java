package com.tedis.client.handler;

import com.tedis.client.connection.AbstractCommonConn;
import com.tedis.protocol.RESPData;
import com.tedis.protocol.Result;
import com.tedis.protocol.Results;
import com.tedis.protocol.codec.ParseInfo;
import com.tedis.protocol.codec.RESPDataParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ResponseDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(ResponseDecoder.class);
    private static StringBuilder logStr = new StringBuilder();
    private static List<Result> results = new ArrayList<>();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        Channel channel = ctx.channel();
        int resultNum = channel.attr(AbstractCommonConn.RESULT_KEY).get();
        if (resultNum == -1) {
            decodeResult(in, out);
        } else {
            decodeResults(in, out, resultNum);
        }
    }

    private Result decode(ByteBuf in) {
        byte[] bytes = ByteBufUtil.getBytes(in);
        Result result = null;
        ParseInfo res = null;
        switch ((char) bytes[0]) {
            case RESPData.ARRAY_PREFIX: {
                res = RESPDataParser.parseArray(bytes, 1);
                result = new Result(RESPData.ARRAY_TYPE, res.getResult());
                break;
            }
            case RESPData.INTEGER_PREFIX: {
                res = RESPDataParser.parseInteger(bytes, 1);
                result = new Result(RESPData.INTEGER_TYPE, res.getResult());
                break;
            }
            case RESPData.BULK_STRING_PREFIX: {
                res = RESPDataParser.parseBulkString(bytes, 1);
                result = new Result(RESPData.BULK_STRING_TYPE, res.getResult());
                break;
            }
            case RESPData.SIMPLE_STRING_PREFIX: {
                res = RESPDataParser.parseSimpleString(bytes, 1);
                result = new Result(RESPData.SIMPLE_STRING_TYPE, res.getResult());
                break;
            }
            case RESPData.ERROR_PREFIX: {
                res = RESPDataParser.parseError(bytes, 1);
                String[] error = res.getResult().split(" ");
                StringBuilder temp = new StringBuilder();
                for (int i = 1; i < error.length; i++) {
                    temp.append(error[i]);
                    if (i < error.length - 1) {
                        temp.append(" ");
                    }
                }
                result = new Result(RESPData.ERROR_TYPE, error[0], temp.toString());
            }
        }
        assert result != null;
        if (result.getResult() == null) {
            return null;
        }
        logDecoder(in, res.getRawLen(), result);
        // 消费ByteBuf
        int curRdIdx = in.readerIndex();
        in.readerIndex(curRdIdx + res.getRawLen());
        return result;
    }

    private void decodeResult(ByteBuf in, List<Object> out) {
        Result result = decode(in);
        if (result != null) {
            out.add(result);
            log.info("Result arrived => {}({})", logStr.toString(), result);
            logStr = new StringBuilder();
        }
    }

    private void decodeResults(ByteBuf in, List<Object> out, int resultNum) {
        Result result = decode(in);
        if (result != null) {
            results.add(result);
        }
        if (results.size() == resultNum) {
            out.add(new Results(results));
            log.info("Results arrived => {}({})", logStr.toString(), results);
            logStr = new StringBuilder();
            results = new ArrayList<>();
        }
    }

    private void logDecoder(ByteBuf buf, int len, Result res) {
        byte[] bytes = ByteBufUtil.getBytes(buf, buf.readerIndex(), len);
        for (byte b : bytes) {
            if (b == (byte) '\r') {
                logStr.append("\\r");
            } else if (b == (byte) '\n') {
                logStr.append("\\n");
            } else {
                logStr.append((char) b);
            }
        }
    }
}
