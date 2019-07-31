package com.tedis.api;

import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.TedisPool;
import io.netty.channel.Channel;


public interface Connection {

    /**
     * PING [message]
     * @param msg message
     * @return pong if msg == null, otherwise msg
     */
    TedisFuture<String> ping(String msg);

    /**
     * Redis command: AUTH password
     *
     * @param pass password
     * @return OK if password matches, otherwise error msg
     */
    TedisFuture<String> auth(String pass);

    /**
     * Redis command: SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
     *
     * @param key      key
     * @param value    value
     * @param optional optional args
     * @return OK if SET was executed correctly, else nil
     */
    TedisFuture<String> set(String key, String value, String... optional);

    /**
     * Redis command: GET key
     *
     * @param key key
     * @return the value of key, or nil when key does not exist.
     */
    TedisFuture<String> get(String key);

    /**
     * Redis command: SETNX key value
     *
     * @param key key
     * @param value value
     * @return 0 if key already exists, else 1
     */
    TedisFuture<String> setnx(String key, String value);

    /**
     * Redis command: INCR key
     *
     * @param key key
     * @return the value of key after the increment
     */
    TedisFuture<String> incr(String key);

    /**
     * Redis command: DEL key [key ...]
     *
     * @param keys keys to remove
     * @return The number of keys that were removed.
     */
    TedisFuture<String> del(String... keys);

    /**
     * Redis command: HMSET key field value [field value ...]
     * @param key name of hashtable
     * @param pairs pairs to set
     * @return
     */
    TedisFuture<String> hmset(String key, String...  pairs);

    /**
     * Redis command: EVAL script numkeys key [key ...] arg [arg ...]
     *
     * @param script Lua 5.1 script
     * @param numKeys number of keys
     * @param keys keys
     * @return
     *
     */
    TedisFuture<String> eval(String script, int numKeys, String... keys);

    /**
     * Redis command: TTL key
     *
     * @param key key
     * @return TTL in seconds
     *         -1 if the key exists but has no associated expire
     *         -2 if the key does not exist.
     */
    TedisFuture<String> ttl(String key);

    void close();

    void returnToPool(TedisPool pool);

    Channel channel();
}
