package com.tedis.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BloomFilterTest {

    @Test
    public void bloomfilterTest() {
        BloomFilter bf = new BloomFilter(1000, 0.001);
        bf.add("Hello World");
        assertTrue(bf.include("Hello World"));
        assertFalse(bf.include("HelloWorld"));
    }
}