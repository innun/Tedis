package com.tedis.common;

import java.net.MalformedURLException;
import java.net.URL;

public class TedisURL {
    private String host;
    private int port;

    public TedisURL(String url) {
        if (!url.startsWith("redis://")) {
            throw new IllegalArgumentException("Url should start with redis://");
        }
        String s = url.replace("redis://", "http://");
        try {
            URL u = new URL(s);
            host = u.getHost();
            port = u.getPort();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
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

    @Override
    public String toString() {
        return "redis://" + host + ":" + port;
    }
}
