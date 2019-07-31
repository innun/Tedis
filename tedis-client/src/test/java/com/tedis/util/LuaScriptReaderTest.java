package com.tedis.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuaScriptReaderTest {

    @Test
    void read() {
        String script = LuaScriptReader.read("unlock.lua");
        System.out.println(script);
    }
}