package com.tedis.client;

public interface Connection {

    String set(String key, String value);
    String get(String key);
}
