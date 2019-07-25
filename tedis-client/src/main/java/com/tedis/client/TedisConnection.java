package com.tedis.client;

import com.tedis.client.api.Connection;
import com.tedis.client.common.Cmd;
import com.tedis.protocol.Request;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TedisConnection implements Connection {
    private static Logger log = LoggerFactory.getLogger(TedisConnection.class);

    private final Channel channel;
    private CompletableFuture<String> future;
    public static final AttributeKey<CompletableFuture<String>> key = AttributeKey.valueOf("future");

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

    private String execute(Cmd cmd, String... params) {
        future = new CompletableFuture<>();
        String cmdName = cmd.getCmd();
        List<String> cmdParts = new ArrayList<>();
        cmdParts.add(cmdName);
        cmdParts.addAll(Arrays.asList(params));
        Request req;
        String result = null;
        try {
            req = new Request(cmdParts);
            channel.attr(key).set(future);
            channel.writeAndFlush(req);
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
