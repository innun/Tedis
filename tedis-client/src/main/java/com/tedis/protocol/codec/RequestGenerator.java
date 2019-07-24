package com.tedis.protocol.codec;

import com.tedis.client.exception.RequestErrorException;
import com.tedis.protocol.RESPData;
import com.tedis.protocol.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class RequestGenerator {

    private static final String CRLF = "\r\n";

    private static byte[] bulkStringToBytes(String str) throws IOException{
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write((byte) RESPData.BULK_STRING_PREFIX);
            baos.write(String.valueOf(str.length()).getBytes());
            baos.write(CRLF.getBytes());
            baos.write(str.getBytes());
            baos.write(CRLF.getBytes());
            return baos.toByteArray();
        }
    }

    public static Request generate(List<String> members) throws RequestErrorException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write((byte) RESPData.ARRAY_PREFIX);
            int num = members.size();
            baos.write(String.valueOf(num).getBytes());
            baos.write(CRLF.getBytes());
            for (String m : members) {
                byte[] element = bulkStringToBytes(m);
                baos.write(element);
            }
            return new Request(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RequestErrorException();
        }
    }
}
