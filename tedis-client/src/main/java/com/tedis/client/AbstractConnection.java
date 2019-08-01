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

public abstract class AbstractConnection<T> implements Connection<T> {

    final Channel channel;
    public static final AttributeKey<Integer> RESULT_NUM_KEY = AttributeKey.valueOf("result_num");

    AbstractConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public TedisFuture<T> ping(String msg) {
        return execute(Cmd.PING, msg);
    }

    @Override
    public TedisFuture<T> auth(String pass) {
        return execute(Cmd.AUTH, pass);
    }

    @Override
    public TedisFuture<T> set(String key, String value, String... optional) {
        String[] params = new String[optional.length + 2];
        params[0] = key;
        params[1] = value;
        System.arraycopy(optional, 0, params, 2, params.length - 2);
        return execute(Cmd.SET, params);
    }

    @Override
    public TedisFuture<T> get(String key) {
        return execute(Cmd.GET, key);
    }

    @Override
    public TedisFuture<T> setnx(String key, String value) {
        return execute(Cmd.SETNX, key, value);
    }

    @Override
    public TedisFuture<T> incr(String key) {
        return execute(Cmd.INCR, key);
    }

    @Override
    public TedisFuture<T> del(String... keys) {
        return execute(Cmd.DEL, keys);
    }

    @Override
    public TedisFuture<T> hmset(String key, String... pairs) {
        String[] params = new String[pairs.length + 1];
        params[0] = key;
        System.arraycopy(pairs, 0, params, 1, pairs.length);
        return execute(Cmd.HMSET, params);
    }

    @Override
    public TedisFuture<T> eval(String script, int numKeys, String... keys) {
        String[] params = new String[2 + keys.length];
        params[0] = script;
        params[1] = String.valueOf(numKeys);
        System.arraycopy(keys,0, params, 2, params.length - 2);
        return execute(Cmd.EVAL, params);
    }

    @Override
    public TedisFuture<T> setbit(String key, long offset, int value) {
        return execute(Cmd.SETBIT, key, String.valueOf(offset), String.valueOf(value));
    }

    @Override
    public TedisFuture<T> getbit(String key, long offset) {
        return execute(Cmd.GETBIT, key, String.valueOf(offset));
    }

    @Override
    public TedisFuture<T> ttl(String key) {
        return execute(Cmd.TTL, key);
    }

    abstract TedisFuture<T> execute(Cmd cmd, String... params);

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
        pool.returnToPool(this);
    }
}
