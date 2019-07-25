package com.tedis.protocol;


import java.util.List;

public class Request {
    private List<String> cmdParts;

    public Request(List<String> cmdParts) {
        this.cmdParts = cmdParts;
    }

    public List<String> getCmdParts() {
        return cmdParts;
    }

    public void setCmdParts(List<String> cmdParts) {
        this.cmdParts = cmdParts;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request{");
        for (int i = 0; i < cmdParts.size(); i++) {
            sb.append(cmdParts.get(i));
            if (i < cmdParts.size() - 1) {
                sb.append(" ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
