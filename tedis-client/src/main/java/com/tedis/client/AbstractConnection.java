package com.tedis.client;

import com.tedis.api.Connection;
import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.TedisPool;
import com.tedis.protocol.Command;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractConnection implements Connection {

    final Channel channel;
    public static final AttributeKey<TedisFuture<String>> KEY = AttributeKey.valueOf("future");

    AbstractConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public TedisFuture<String> ping(String msg) {
        return execute(Cmd.PING, msg);
    }

    @Override
    public TedisFuture<String> auth(String pass) {
        return execute(Cmd.AUTH, pass);
    }

    @Override
    public TedisFuture<String> set(String key, String value, String... optional) {
        String[] params = new String[optional.length + 2];
        params[0] = key;
        params[1] = value;
        System.arraycopy(optional, 0, params, 2, params.length - 2);
        return execute(Cmd.SET, params);
    }

    @Override
    public TedisFuture<String> get(String key) {
        return execute(Cmd.GET, key);
    }

    @Override
    public TedisFuture<String> setnx(String key, String value) {
        return execute(Cmd.SETNX, key, value);
    }

    @Override
    public TedisFuture<String> incr(String key) {
        return execute(Cmd.INCR, key);
    }

    @Override
    public TedisFuture<String> del(String... keys) {
        return execute(Cmd.DEL, keys);
    }

    @Override
    public TedisFuture<String> hmset(String key, String... pairs) {
        String[] params = new String[pairs.length + 1];
        params[0] = key;
        System.arraycopy(pairs, 0, params, 1, pairs.length);
        return execute(Cmd.HMSET, params);
    }

    @Override
    public TedisFuture<String> eval(String script, int numKeys, String... keys) {
        String[] params = new String[2 + keys.length];
        params[0] = script;
        params[1] = String.valueOf(numKeys);
        System.arraycopy(keys,0, params, 2, params.length - 2);
        return execute(Cmd.EVAL, params);
    }

    @Override
    public TedisFuture<String> ttl(String key) {
        return execute(Cmd.TTL, key);
    }

    abstract TedisFuture<String> execute(Cmd cmd, String... params);

    Command generateCmd(Cmd cmd, String... params) {
        String cmdName = cmd.getCmd();
        List<String> parts = new ArrayList<>();
        parts.add(cmdName);
        parts.addAll(Arrays.asList(params));
        return new Command(parts);
    }

    public Channel channel() {
        return channel;
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void returnToPool(TedisPool pool) {
        pool.returnConn(this);
    }
}
