package com.tedis.client.common;

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
    PING("PING");


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
