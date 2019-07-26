package com.tedis.client.common;

public enum Cmd {
    SET("SET"),
    GET("GET"),
    SETNX("SETNX"),
    INCR("INCR"),
    AUTH("AUTH"),
    DEL("DEL");


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
