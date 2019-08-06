package com.tedis.tools;

import com.tedis.Tedis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BloomFilterTest {

    @Test
    public void bloomfilterTest() {
        Tedis tedis = new Tedis();
        BloomFilter bf = tedis.newBloomFilter(1000, 0.1);
        bf.add("HELLO WORLD");
        assertTrue(bf.include("HELLO WORLD"));
        assertFalse(bf.include("HELLO WORLd"));
    }
}