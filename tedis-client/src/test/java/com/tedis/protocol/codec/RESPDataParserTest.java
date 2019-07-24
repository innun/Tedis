package com.tedis.protocol.codec;

import org.junit.jupiter.api.Test;

import static com.tedis.protocol.codec.RESPDataParser.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RESPDataParserTest {
    @Test
    public void parseSimpleStringTest() {
        byte[] a = "+OK\r\n".getBytes();
        byte[] b = "+\r\n".getBytes();
        byte[] c = "+Hello World\r\n".getBytes();
        assertEquals(RESPDataParser.parseSimpleString(a, 1).getResult(), "\"OK\"");
        assertEquals(RESPDataParser.parseSimpleString(b, 1).getResult(), "\"\"");
        assertEquals(RESPDataParser.parseSimpleString(c, 1).getResult(), "\"Hello World\"");
    }

    @Test
    public void parseErrorTest() {
        byte[] a = "-Error message\r\n".getBytes();
        byte[] b = "-ERR unknown command 'foobar'\r\n".getBytes();
        byte[] c = "-WRONGTYPE Operation against a key holding the wrong kind of value\r\n".getBytes();
        assertEquals(parseError(a, 1).getResult(), "Error message");
        assertEquals(parseError(b, 1).getResult(), "ERR unknown command 'foobar'");
        assertEquals(parseError(c, 1).getResult(), "WRONGTYPE Operation against a key holding the wrong kind of value");
    }

    @Test
    public void parseIntegerTest() {
        byte[] a = ":1000\r\n".getBytes();
        byte[] b = ":-1\r\n".getBytes();
        assertEquals(parseInteger(a,1).getResult(), "1000");
        assertEquals(parseInteger(b, 1).getResult(), "-1");
    }

    @Test
    public void parseBulkStringTest() {
        byte[] a = "$6\r\nfoobar\r\n".getBytes();
        byte[] b = "$-1\r\n".getBytes();
        byte[] c = "$0\r\n\r\n".getBytes();
        byte[] d = "$11\r\nHello World\r\n".getBytes();
        assertEquals(parseBulkString(a,1).getResult(), "\"foobar\"");
        assertEquals(parseBulkString(b, 1).getResult(), "nil");
        assertEquals(parseBulkString(c, 1).getResult(), "\"\"");
        assertEquals(parseBulkString(d, 1).getResult(), "\"Hello World\"");
    }

    @Test
    public void parseArrayTest() {
        byte[] a = "*-1\r\n".getBytes();
        byte[] b = "*0\r\n".getBytes();
        byte[] c = "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n".getBytes();
        byte[] d = "*3\r\n:1\r\n:2\r\n:3\r\n".getBytes();
        byte[] e = "*5\r\n:1\r\n:2\r\n:3\r\n:4\r\n$6\r\nfoobar\r\n".getBytes();
        byte[] f = "*2\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+Foo\r\n-Err msg\r\n".getBytes();
        assertEquals(parseArray(a, 1).getResult(), "nil");
        assertEquals(parseArray(b, 1).getResult(), "[]");
        assertEquals(parseArray(c, 1).getResult(), "[\"foo\", \"bar\"]");
        assertEquals(parseArray(d, 1).getResult(), "[1, 2, 3]");
        assertEquals(parseArray(e, 1).getResult(), "[1, 2, 3, 4, \"foobar\"]");
        assertEquals(parseArray(f, 1).getResult(), "[[1, 2, 3], [\"Foo\", Err msg]]");
    }
}
