package com.tedis.util;

import java.io.*;

public class LuaScriptReader {
    public static String read(String filename) {
        InputStream is = LuaScriptReader.class.getResourceAsStream("/" + filename);
        StringBuilder sb = new StringBuilder();
        char[] cbuf = new char[1024];
        try (InputStreamReader isr = new InputStreamReader(is)) {
            int len;
            while ((len = isr.read(cbuf)) >= 0) {
                sb.append(cbuf, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
