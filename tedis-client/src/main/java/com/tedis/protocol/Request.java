package com.tedis.protocol;


public class Request {
    private byte[] payload;

    public Request(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : payload) {
            if (b == '\r') {
                sb.append("\\r");
            } else if (b == '\n') {
                sb.append("\\n");
            } else {
                sb.append((char)b);
            }
        }
        return sb.toString();
    }
}
