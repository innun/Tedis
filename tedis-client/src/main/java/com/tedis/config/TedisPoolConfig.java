package com.tedis.config;

public class TedisPoolConfig {
    private int coreConns;
    private int maxConns;

    public static final TedisPoolConfig DEFAULT_TEDIS_POOL_CONFIG =
            new TedisPoolConfig(4, Integer.MAX_VALUE);

    public TedisPoolConfig(int coreConns, int maxConns) {
        this.coreConns = coreConns;
        this.maxConns = maxConns;
    }

    public int getCoreConns() {
        return coreConns;
    }

    public void setCoreConns(int coreConns) {
        this.coreConns = coreConns;
    }

    public int getMaxConns() {
        return maxConns;
    }

    public void setMaxConns(int maxConns) {
        this.maxConns = maxConns;
    }
}
