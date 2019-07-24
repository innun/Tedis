package com.tedis.protocol.codec;

public class ParseInfo {
    private int rawLen;
    private String result;

    ParseInfo(int rawLen, String result) {
        this.rawLen = rawLen;
        this.result = result;
    }

    public int getRawLen() {
        return rawLen;
    }

    public String getResult() {
        return result;
    }
}