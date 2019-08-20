package com.tedis.benchmark;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Test {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.103.2.229");
        jedis.auth("980608");
        new Thread(() -> {
            jedis.subscribe(new Subscriber(), "news");
        }).start();
        jedis.set("a", "189");
        System.out.println(jedis.get("a"));
    }
}

class Subscriber extends JedisPubSub {
    public Subscriber() {
    }

    public void onMessage(String channel, String message) {
        System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
    }

    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
