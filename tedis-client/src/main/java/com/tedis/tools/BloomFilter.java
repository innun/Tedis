package com.tedis.tools;

import com.tedis.client.Pipeline;
import com.tedis.protocol.Result;
import com.tedis.protocol.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloomFilter {
    Logger log = LoggerFactory.getLogger(BloomFilter.class);

    private long size;
    private int hashFuncs;
    private static final String BLOOM_FILTER = "BLOOMFILTER";
    private Pipeline p;

    public BloomFilter(long insertions, double falseProbability, Pipeline p) {
        if (falseProbability <= 0 || falseProbability > 1 || insertions < 0) {
            throw new IllegalArgumentException("illegal argument");
        }
        this.size = optimalSize(insertions, falseProbability);
        this.hashFuncs = optimalHashFuncs(size, insertions);
        this.p = p;
        log.info("BloomFilter init => size: {} hash functions: {}", size, hashFuncs);
    }

    /**
     *
     * @param n insertions
     * @param p false probability
     * @return -(n*lnp/(ln2)^2)
     */
    private long optimalSize(long n, double p) {
        return (long) -(n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     *
     * @param m size
     * @param n insertions
     * @return m/n*ln2
     */
    private int optimalHashFuncs(long m, long n) {
        return (int) Math.round((double) m / n * Math.log(2));
    }

    public void add(String str) {
        long[] hashes = getHashes(str);
        for (int i = 0; i < hashFuncs; i++) {
            long index = hashes[i] % size;
            p.setbit(BLOOM_FILTER, index, 1);
        }
        p.submit().sync();
    }

    public boolean include(String str) {
        long[] hashes = getHashes(str);
        for (int i = 0; i < hashFuncs; i++) {
            long index = hashes[i] % size;
            p.getbit(BLOOM_FILTER, index);
        }
        Results results = p.submit().sync();
        for (Result result : results) {
            if (result.getResult().equals("0")) {
                return false;
            }
        }
        return true;
    }

    private long[] getHashes(String str) {
        long[] hashes = new long[hashFuncs];
        for (int i = 0; i < hashFuncs; i++) {
            hashes[i] = hash(str, i);
        }
        return hashes;
    }

    private long hash(String str, int factor) {
        long h = 0;
        for (int i = 0; i < str.length(); i++) {
            h = h * (31 + factor) + str.charAt(i);
        }
        return h;
    }

    public long getSize() {
        return size;
    }

    public int getHashFuncs() {
        return hashFuncs;
    }

    public void close() {
        this.p.returnToPool();
    }
}
