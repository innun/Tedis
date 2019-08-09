package com.tedis.config;

import com.tedis.common.TedisURL;

public class TedisClientConfig {
    public static final String PROP_PREFIX = "com.tedis.client.";
    private TedisURL tedisURL;
    private String password;

    private TedisClientConfig() {}

    public static TedisClientConfig build() {
        return new TedisClientConfig();
    }


    public TedisClientConfig password(String password) {
        this.password = password;
        return this;
    }

    public TedisClientConfig url(String url) {
        this.tedisURL = new TedisURL(url);
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

}
