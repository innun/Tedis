package com.tedis.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UniqueCodeGenerator {

    public static String uniqueCode() {
        long curTime = System.currentTimeMillis();
        InetAddress localAddr = null;
        try {
            localAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        assert localAddr != null;
        return curTime + localAddr.getHostName() + "@" + localAddr.getHostAddress();
    }
}
