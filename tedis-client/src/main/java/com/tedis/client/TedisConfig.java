package com.tedis.client;

public class TedisConfig {
    private String host;
    private int port;
    private String password;
    public static final TedisConfig DEFAULT_CONFIG = new TedisConfig("47.103.2.229", 6379, "980608");

    private TedisConfig() {}

    private TedisConfig(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public static TedisConfig build() {
        return new TedisConfig();
    }

    public TedisConfig host(String host) {
        this.host = host;
        return this;
    }

    public TedisConfig port(int port) {
        this.port = port;
        return this;
    }

    public TedisConfig password(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
