package com.tedis.tools;

import com.tedis.client.Pipeline;
import com.tedis.client.TedisClientConfig;
import com.tedis.client.pool.TedisPool;
import com.tedis.client.pool.TedisPoolConfig;

public class BloomFilter {

    private long size;
    private int hashFuncs;
    private static final String BLOOM_FILTER = "BLOOMFILTER";
    private static Pipeline p;

    public BloomFilter(long insertions, double falseProbability) {
        if (falseProbability <= 0 || falseProbability > 1 || insertions < 0) {
            throw new IllegalArgumentException("illegal argument");
        }
        this.size = optimalSize(insertions, falseProbability);
        this.hashFuncs = optimalHashFuncs(size, insertions);
        TedisPool pool = new TedisPool(
                TedisPoolConfig.DEFAULT_TEDIS_POOL_CONFIG,
                TedisClientConfig.DEFAULT_CONFIG);
        p = pool.pipeline();
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
        for (int i = 1; i < hashFuncs; i++) {
            long index = hashes[i] % size;
            p.setbit(BLOOM_FILTER, index, 1);
        }
        p.submit();
    }

//    public boolean include(String str) {
//    }

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


}
