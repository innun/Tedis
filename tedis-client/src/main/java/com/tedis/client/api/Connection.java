package com.tedis.client.api;

public interface Connection {

    String set(String key, String value);
    String get(String key);
}
