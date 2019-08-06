package com.tedis;

import com.tedis.api.Command;
import com.tedis.client.Pipeline;
import com.tedis.client.TedisConnection;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.TedisPool;
import com.tedis.protocol.Results;

public class Tedis implements Command {
    public static final int TRADITIONAL = 0;
    public static final int PIPELINE = 1;

    private TedisPool pool;
    private int mode;
    private Pipeline p;
    private TedisConnection c;

    public Tedis() {
        init();
    }

    private void init() {
        mode = Tedis.TRADITIONAL;
        pool = TedisPool.pool();
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == Tedis.PIPELINE) {
            if (c != null) {
                pool.returnToPool(c);
            }
            p = pool.pipeline();
        } else if (mode == Tedis.TRADITIONAL) {
            if (p != null) {
                pool.returnToPool(p);
            }
            c = pool.connection();
        }
    }

    public TedisFuture<Results> submit() {
        if (mode != Tedis.PIPELINE) {
            throw new RuntimeException("tedis's mode is not pipeline");
        }
        return p.submit();
    }

    private boolean traditional() {
        return mode == Tedis.TRADITIONAL;
    }

    @Override
    public TedisFuture ping(String... msg) {
        return traditional() ? c.ping(msg) : p.ping(msg);
    }

    @Override
    public TedisFuture auth(String pass) {
        return traditional() ? c.auth(pass) : p.auth(pass);
    }

    @Override
    public TedisFuture set(String key, String value, String... optional) {
        return traditional()
                ? c.set(key, value, optional)
                : p.set(key, value, optional);
    }

    @Override
    public TedisFuture get(String key) {
        return traditional() ? c.get(key) : p.get(key);
    }

    @Override
    public TedisFuture setnx(String key, String value) {
        return traditional() ? c.setnx(key, value) : p.setnx(key, value);
    }

    @Override
    public TedisFuture incr(String key) {
        return traditional() ? c.incr(key) : p.incr(key);
    }

    @Override
    public TedisFuture del(String... keys) {
        return traditional() ? c.del(keys) : p.del(keys);
    }

    @Override
    public TedisFuture hmset(String key, String... pairs) {
        return traditional() ? c.hmset(key, pairs) : p.hmset(key, pairs);
    }

    @Override
    public TedisFuture eval(String script, int numKeys, String... args) {
        return traditional()
                ? c.eval(script, numKeys, args)
                : p.eval(script, numKeys, args);
    }

    @Override
    public TedisFuture setbit(String key, long offset, int value) {
        return traditional()
                ? c.setbit(key, offset, value)
                : p.setbit(key, offset, value);
    }

    @Override
    public TedisFuture getbit(String key, long offset) {
        return traditional() ? c.getbit(key, offset) : p.getbit(key, offset);
    }

    @Override
    public TedisFuture ttl(String key) {
        return traditional() ? c.ttl(key) : p.ttl(key);
    }

    public void close() {
        if (c != null) {
            c.close();
        }
        if (p != null) {
            p.close();
        }
        pool.close();
    }
}
