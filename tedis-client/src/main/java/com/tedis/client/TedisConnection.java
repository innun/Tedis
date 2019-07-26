package com.tedis.client;

import com.tedis.api.Connection;
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
    public String auth(String pass) {
        return execute(Cmd.AUTH, pass);
    }

    @Override
    public String set(String key, String value, String... optional) {
        String[] params = new String[optional.length + 2];
        params[0] = key;
        params[1] = value;
        System.arraycopy(optional, 0, params, 2, params.length - 2);
        return execute(Cmd.SET, params);
    }

    @Override
    public String get(String key) {
        return execute(Cmd.GET, key);
    }

    @Override
    public String setnx(String key, String value) {
        return execute(Cmd.SETNX, key, value);
    }

    @Override
    public String incr(String key) {
        return execute(Cmd.INCR, key);
    }

    @Override
    public String del(String... keys) {
        return execute(Cmd.DEL, keys);
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
