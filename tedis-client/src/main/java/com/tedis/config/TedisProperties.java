package com.tedis.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TedisProperties {
    private static final int CR = '\r';
    private static final int LF = '\n';
    private static final int SPC = ' ';
    private static final int TAB = '\t';
    private static final int EQU = '=';
    private static final HashSet<Integer> cset;

    private static final String FILENAME = "tedis.properties";
    private HashMap<String, String> properties;
    private InputStreamReader isr;
    private boolean isOver;
    private Buffer buffer;

    static {
        cset = new HashSet<>();
        cset.add(CR);
        cset.add(LF);
        cset.add(SPC);
        cset.add(TAB);
        cset.add(EQU);
    }

    private class Buffer {
        private char[] buf = new char[1024];
        private int idx = 0;
        private int size = 0;

        public Buffer() {
            fill();
        }

        public void readProperty() {
            String key = readKey();
            String val = readVal();
            properties.put(key, val);
        }

        private void skipSpace() {
            while (!isOver && cset.contains((int) buf[idx])) {
                idx++;
                checkBuf();
            }
        }
        
        private void skipComment() {
            while (!isOver && buf[idx] != LF) {
                idx++;
                checkBuf();
            }
        }
        
        private String readKey() {
            skipSpace();
            if (buf[idx] == '#') {
                skipComment();
            }
            StringBuilder sb = new StringBuilder();
            while (!isOver && !cset.contains((int) buf[idx]) && buf[idx] != '=') {
                sb.append(buf[idx]);
                idx++;
                checkBuf();
            }
            return sb.toString();
        }
        
        private String readVal() {
            skipSpace();
            StringBuilder sb = new StringBuilder();
            while (!isOver && buf[idx] != CR && buf[idx] != LF) {
                sb.append(buf[idx]);
                idx++;
                checkBuf();
            }
            return sb.toString();
        }
        
        private void checkBuf() {
            if (idx >= size) {
                fill();
            }
        }

        private void fill() {
            int len;
            try {
                if ((len = isr.read(buf)) == -1) {
                    isOver = true;
                    size = 0;
                } else {
                    size = len;
                }
                idx = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TedisProperties() {
        properties = new HashMap<>();
        load();
        buffer = new Buffer();
        while (!isOver) {
            buffer.readProperty();
        }
    }

    private void load() {
        InputStream is = TedisProperties.class.getClassLoader().getResourceAsStream(FILENAME);
        if (is == null) {
            throw new RuntimeException("tedis config file not found");
        }
        isr = new InputStreamReader(is, StandardCharsets.UTF_8);
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }
}
