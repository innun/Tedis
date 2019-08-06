package com.tedis.config;

public class TedisPoolConfig {
    public static final String PROP_PREFIX = "com.tedis.pool.";
    private int coreConns;
    private int maxConns;

    private TedisPoolConfig() {}

    public static TedisPoolConfig build() {
        return new TedisPoolConfig();
    }

    public TedisPoolConfig coreConns(String coreConns) {
        this.coreConns = Integer.parseInt(coreConns);
        return this;
    }

    public TedisPoolConfig maxConns(String maxConns) {
        this.maxConns = Integer.parseInt(maxConns);
        return this;
    }

    public int getCoreConns() {
        return coreConns;
    }

    public int getMaxConns() {
        return maxConns;
    }
}
