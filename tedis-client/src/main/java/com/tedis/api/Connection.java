package com.tedis.api;

public interface Connection {

    /**
     * Redis command: AUTH password
     *
     * @param pass password
     * @return OK if password matches, otherwise error msg
     */
    String auth(String pass);

    /**
     * Redis command: SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
     *
     * @param key      key
     * @param value    value
     * @param optional optional args
     * @return OK if SET was executed correctly, else nil
     */
    String set(String key, String value, String... optional);

    /**
     * Redis command: GET key
     *
     * @param key key
     * @return the value of key, or nil when key does not exist.
     */
    String get(String key);

    /**
     * Redis command: SETNX key value
     *
     * @param key key
     * @param value value
     * @return 0 if key already exists, else 1
     */
    String setnx(String key, String value);

    /**
     * Redis command: INCR key
     *
     * @param key key
     * @return the value of key after the increment
     */
    String incr(String key);

    /**
     * Redis command: DEL key [key ...]
     *
     * @param keys keys to remove
     * @return The number of keys that were removed.
     */
    String del(String... keys);

}
