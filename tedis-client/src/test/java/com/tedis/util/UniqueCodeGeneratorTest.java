package com.tedis.util;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class UniqueCodeGeneratorTest {

    @Test
    void uniqueCode() throws UnknownHostException {
        System.out.println(UniqueCodeGenerator.uniqueCode());
    }
}