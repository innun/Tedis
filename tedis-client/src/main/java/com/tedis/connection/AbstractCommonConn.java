package com.tedis.connection;

import com.tedis.common.Cmd;
import com.tedis.common.TedisFuture;
import com.tedis.pool.ConnPool;
import io.netty.channel.Channel;

import java.util.Arrays;

public abstract class AbstractCommonConn<T> extends AbstractBaseConn<T> implements CommonCmd<T> {

    public AbstractCommonConn(ConnPool pool, Channel channel) {
        super(pool, channel);
    }

    @Override
    public TedisFuture<T> ping(String... msg) {
        return send(Cmd.PING, msg);
    }

    @Override
    public TedisFuture<T> auth(String pass) {
        return send(Cmd.AUTH, pass);
    }

    @Override
    public TedisFuture<T> set(String key, String value, String... optional) {
        String[] params = new String[optional.length + 2];
        params[0] = key;
        params[1] = value;
        System.arraycopy(optional, 0, params, 2, params.length - 2);
        return send(Cmd.SET, params);
    }

    @Override
    public TedisFuture<T> get(String key) {
        return send(Cmd.GET, key);
    }

    @Override
    public TedisFuture<T> setnx(String key, String value) {
        return send(Cmd.SETNX, key, value);
    }

    @Override
    public TedisFuture<T> incr(String key) {
        return send(Cmd.INCR, key);
    }

    @Override
    public TedisFuture<T> del(String... keys) {
        return send(Cmd.DEL, keys);
    }

    @Override
    public TedisFuture<T> hmset(String key, String... pairs) {
        String[] params = new String[pairs.length + 1];
        params[0] = key;
        System.arraycopy(pairs, 0, params, 1, pairs.length);
        return send(Cmd.HMSET, params);
    }

    @Override
    public TedisFuture<T> eval(String script, int numKeys, String... keys) {
        String[] params = new String[2 + keys.length];
        params[0] = script;
        params[1] = String.valueOf(numKeys);
        System.arraycopy(keys,0, params, 2, params.length - 2);
        return send(Cmd.EVAL, params);
    }

    @Override
    public TedisFuture<T> setbit(String key, long offset, int value) {
        return send(Cmd.SETBIT, key, String.valueOf(offset), String.valueOf(value));
    }

    @Override
    public TedisFuture<T> getbit(String key, long offset) {
        return send(Cmd.GETBIT, key, String.valueOf(offset));
    }

    @Override
    public TedisFuture<T> ttl(String key) {
        return send(Cmd.TTL, key);
    }

    @Override
    public TedisFuture<T> publish(String channel, String msg) {
        return send(Cmd.PUBLISH, (String[]) Arrays.asList(channel, msg).toArray());
    }

    @Override
    public TedisFuture<T> pubsub(String subcommand, String... args) {
        return send(Cmd.PUBSUB, (String[]) Arrays.asList(subcommand, args).toArray());
    }
}
