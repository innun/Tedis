package com.tedis.client.handler;

import com.tedis.client.exception.RequestErrorException;
import com.tedis.protocol.Command;
import com.tedis.protocol.Commands;
import com.tedis.protocol.RESPData;
import com.tedis.protocol.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class RequestEncoder extends MessageToByteEncoder<Request> {

    private static final Logger log = LoggerFactory.getLogger(RequestEncoder.class);

    private static final String CRLF = "\r\n";

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) {
        try {
            Object payload = msg.getPayload();
            if (payload instanceof Command) {
                Command cmd = (Command) payload;
                byte[] bytes = encode(cmd);
                out.writeBytes(bytes);
                logEncoder(msg, bytes);
            }
            if (payload instanceof Commands) {
                Commands cmds = (Commands) payload;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (Command cmd : cmds.getCmds()) {
                    byte[] bytes = encode(cmd);
                    baos.write(bytes);
                    out.writeBytes(CRLF.getBytes());
                }
                out.writeBytes(baos.toByteArray());
                logEncoder(msg, baos.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encode(Command cmd) throws RequestErrorException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write((byte) RESPData.ARRAY_PREFIX);
            List<String> parts = cmd.getParts();
            int num = parts.size();
            baos.write(String.valueOf(num).getBytes());
            baos.write(CRLF.getBytes());
            for (String m : parts) {
                byte[] element = bulkStringToBytes(m);
                baos.write(element);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RequestErrorException();
        }
    }

    private static byte[] bulkStringToBytes(String str) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write((byte) RESPData.BULK_STRING_PREFIX);
            baos.write(String.valueOf(str.length()).getBytes());
            baos.write(CRLF.getBytes());
            baos.write(str.getBytes());
            baos.write(CRLF.getBytes());
            return baos.toByteArray();
        }
    }

    @SuppressWarnings("Duplicates")
    private void logEncoder(Request req, byte[] bytes) {
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
        log.info("Request sent => {} ({})", sb.toString(), req);
    }
}
