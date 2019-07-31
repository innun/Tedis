package com.tedis.client;

import com.tedis.client.common.TedisURL;

public class TedisClientConfig {
    private TedisURL tedisURL;
    private String password;
    public static final TedisClientConfig DEFAULT_CONFIG = new TedisClientConfig("redis://47.103.2.229:6379", "980608");

    private TedisClientConfig() {}

    private TedisClientConfig(String url, String password) {
        tedisURL = new TedisURL(url);
        this.password = password;
    }

    public static TedisClientConfig build() {
        return new TedisClientConfig();
    }


    public TedisClientConfig password(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return tedisURL.getHost();
    }

    public int getPort() {
        return tedisURL.getPort();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
