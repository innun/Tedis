package com.tedis.connection;

import com.tedis.common.TedisFuture;

public interface CommonCmd<T> {
    /**
     * PING [message]
     * @param msg message
     * @return pong if msg == null, otherwise msg
     */
    TedisFuture<T> ping(String... msg);

    /**
     * Redis command: AUTH password
     *
     * @param pass password
     * @return OK if password matches, otherwise error msg
     */
    TedisFuture<T> auth(String pass);

    /**
     * Redis command: SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
     *
     * @param key      key
     * @param value    value
     * @param optional optional args
     * @return OK if SET was executed correctly, else nil
     */
    TedisFuture<T> set(String key, String value, String... optional);

    /**
     * Redis command: GET key
     *
     * @param key key
     * @return the value of key, or nil when key does not exist.
     */
    TedisFuture<T> get(String key);

    /**
     * Redis command: SETNX key value
     *
     * @param key key
     * @param value value
     * @return 0 if key already exists, else 1
     */
    TedisFuture<T> setnx(String key, String value);

    /**
     * Redis command: INCR key
     *
     * @param key key
     * @return the value of key after the increment
     */
    TedisFuture<T> incr(String key);

    /**
     * Redis command: DEL key [key ...]
     *
     * @param keys keys to remove
     * @return The number of keys that were removed.
     */
    TedisFuture<T> del(String... keys);

    /**
     * Redis command: HMSET key field value [field value ...]
     * @param key name of hashtable
     * @param pairs pairs to set
     * @return
     */
    TedisFuture<T> hmset(String key, String...  pairs);

    /**
     * Redis command: EVAL script numkeys key [key ...] arg [arg ...]
     *
     * @param script Lua 5.1 script
     * @param numKeys number of keys
     * @param args keys
     * @return
     *
     */
    TedisFuture<T> eval(String script, int numKeys, String... args);

    /**
     * Redis command: SETBIT key offset value
     *
     * @param key bitmap key
     * @param offset offset
     * @param value 0 or 1
     * @return original bit value
     */
    TedisFuture<T> setbit(String key, long offset, int value);

    /**
     * Redis command: GETBIT key offset
     *
     * @param key bitmap key
     * @param offset offset
     * @return bit value stored at offset
     */
    TedisFuture<T> getbit(String key, long offset);

    /**
     * Redis command: TTL key
     *
     * @param key key
     * @return TTL in seconds
     *         -1 if the key exists but has no associated expire
     *         -2 if the key does not exist.
     */
    TedisFuture<T> ttl(String key);

    /**
     * Redis command: PUBLISH channel message
     *
     * @param channel channel to send message
     * @param msg message
     * @return
     */
    TedisFuture<T> publish(String channel, String msg);

    /**
     * Reedis command: PUBSUB subcommand [argument [argument ...]]
     *
     * @param subcommand subcommand
     * @param args args
     * @return result
     */
    TedisFuture<T> pubsub(String subcommand, String... args);
}
