package com.tedis.api;

import com.tedis.annotation.SkipIssueCheck;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.connection.CommonCmd;
import com.tedis.client.connection.SubCmd;
import com.tedis.protocol.Results;
import com.tedis.tools.BloomFilter;
import com.tedis.tools.locks.TedisLock;

public interface Tedis extends CommonCmd, SubCmd {

    @SkipIssueCheck
    void setMode(int mode);

    @SkipIssueCheck
    int getMode();

    @SkipIssueCheck
    TedisFuture<Results> submit();

    @SkipIssueCheck
    void close();

    //***************************tools**************************
    @SkipIssueCheck
    TedisLock newLock();

    @SkipIssueCheck
    TedisLock newLock(int expireTime);

    @SkipIssueCheck
    BloomFilter newBloomFilter(long insertions, double falseProbability);
}
