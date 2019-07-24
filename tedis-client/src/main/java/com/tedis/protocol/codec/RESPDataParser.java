package com.tedis.protocol.codec;

import com.tedis.protocol.RESPData;

public class RESPDataParser {

    private static final int CRLF_LEN = 2;
    private static final String QUOTE = "\"";
    private static final String NULL = "nil";
    private static final String LBRACKET = "[";
    private static final String RBRACKET = "]";
    private static final String DOT = ", ";

    public static ParseInfo parseSimpleString(byte[] bytes, int offset) {
        String s = scanString(bytes, offset);
        int rawLen = 1 + s.length() + CRLF_LEN;
        return new ParseInfo(rawLen, QUOTE + s + QUOTE);
    }

    public static ParseInfo parseError(byte[] bytes, int offset) {
        String s = scanString(bytes, offset);
        int rawLen = 1 + s.length() + CRLF_LEN;
        return new ParseInfo(rawLen, s);
    }

    public static ParseInfo parseInteger(byte[] bytes, int offset) {
        String s = scanString(bytes, offset);
        int rawLen = 1 + s.length() + CRLF_LEN;
        return new ParseInfo(rawLen, s);
    }

    public static ParseInfo parseBulkString(byte[] bytes, int offset) {
        String len = scanString(bytes, offset);
        if (len.equals("-1")) {
            return new ParseInfo(5, NULL);
        }
        String s = scanString(bytes,offset + len.length() + CRLF_LEN);
        int rawLen = 1 + len.length() + CRLF_LEN + s.length() + CRLF_LEN;
        return new ParseInfo(rawLen, QUOTE + s + QUOTE);
    }

    public static ParseInfo parseArray(byte[] bytes, int offset) {
        String elmNum = scanString(bytes, offset);
        int num = Integer.parseInt(elmNum);
        offset = offset + elmNum.length() + CRLF_LEN;
        if (num == -1) {
            return new ParseInfo(5,NULL);
        }
        int rawLen = 1 + elmNum.length() + CRLF_LEN;
        StringBuilder sb = new StringBuilder();
        sb.append(LBRACKET);
        for (int i = 0; i < num; i++) {
            ParseInfo p = null;
            switch ((char) bytes[offset]) {
                case RESPData.SIMPLE_STRING_PREFIX: {
                    p = parseSimpleString(bytes, offset + 1);
                    break;
                }
                case RESPData.INTEGER_PREFIX: {
                    p = parseInteger(bytes, offset + 1);
                    break;
                }
                case RESPData.BULK_STRING_PREFIX: {
                    p = parseBulkString(bytes, offset + 1);
                    break;
                }
                case RESPData.ERROR_PREFIX: {
                    p = parseError(bytes, offset + 1);
                    break;
                }
                case RESPData.ARRAY_PREFIX: {
                    p = parseArray(bytes, offset + 1);
                    break;
                }
            }
            assert p != null;
            offset = offset + p.getRawLen();
            rawLen = rawLen + p.getRawLen();
            sb.append(p.getResult());
            if (i != num - 1) {
                sb.append(DOT);
            }
        }
        sb.append(RBRACKET);
        return new ParseInfo(rawLen, sb.toString());
    }

    private static String scanString(byte[] bytes, int offset) {
        StringBuilder sb = new StringBuilder();
        char b;
        while((b = (char) bytes[offset]) != '\r') {
            sb.append(b);
            offset++;
        }
        return sb.toString();
    }
}
