package com.tedis.common;

public enum Cmd {
    SET("SET"),
    GET("GET");

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
