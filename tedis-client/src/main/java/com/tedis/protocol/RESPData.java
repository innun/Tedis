package com.tedis.protocol;

public class RESPData {
    public static final char SIMPLE_STRING_PREFIX    = '+';      // e.g. "+OK\r\n"
    public static final char ERROR_PREFIX            = '-';      // e.g. "-Error message\r\n"
    public static final char INTEGER_PREFIX          = ':';      // e.g. ":1000\r\n"
    public static final char BULK_STRING_PREFIX      = '$';      // e.g. "$6\r\nfoobar\r\n"  str_len + CRLF + str + CRLF
    public static final char ARRAY_PREFIX            = '*';      // e.g. "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n" elm_num + CRLF + [RESP-TYPE ...] + CRLF
    public static final char NULL_PREFIX             = ' ';      // e.g. "$-1\r\n" or "*-1\r\n"

    public static final int SIMPLE_STRING_TYPE       = 0;
    public static final int ERROR_TYPE               = 1;
    public static final int INTEGER_TYPE             = 2;
    public static final int BULK_STRING_TYPE         = 3;
    public static final int ARRAY_TYPE               = 4;
    public static final int NULL_TYPE                = 5;
}