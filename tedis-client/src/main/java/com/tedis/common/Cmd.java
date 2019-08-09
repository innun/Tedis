package com.tedis.common;

public enum Cmd {
    SET("SET"),
    GET("GET"),
    SETNX("SETNX"),
    INCR("INCR"),
    AUTH("AUTH"),
    DEL("DEL"),
    EVAL("EVAL"),
    TTL("TTL"),
    HMSET("HMSET"),
    PING("PING"),
    SETBIT("SETBIT"),
    GETBIT("GETBIT"),
    SUBSCRIBE("SUBSCRIBE"),
    UNSUBSCRIBE("UNSUBSCRIBE"),
    PSUBSCRIBE("PSUBSCRIBE"),
    PUNSUBSCRIBE("PUNSUBSCRIBE"),
    PUBLISH("PUBLISH"),
    PUBSUB("PUBSUB");


    private String cmd;

    Cmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
