package com.tedis.client;

import com.tedis.client.exception.RequestErrorException;
import com.tedis.common.Cmd;
import com.tedis.protocol.Request;
import com.tedis.protocol.codec.RequestGenerator;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TedisConnection implements Connection {
    private static Logger log = LoggerFactory.getLogger(TedisConnection.class);

    private final Channel channel;

    public TedisConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String set(String key, String value) {
        return execute(Cmd.SET, key, value);
    }

    @Override
    public String get(String key) {
        return execute(Cmd.GET, key);
    }

    private String execute(Cmd cmd, String... parms) {
        String cmdName = cmd.getCmd();
        List<String> cmdParts = new ArrayList<>();
        cmdParts.add(cmdName);
        cmdParts.addAll(Arrays.asList(parms));
        Request req = null;
        try {
            req = RequestGenerator.generate(cmdParts);
            channel.writeAndFlush(req);
            log.info("Request send => {}", req);
        } catch (RequestErrorException e) {
            e.printStackTrace();
        }
        // TODO get the response
        return null;
    }
}
