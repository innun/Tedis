package com.tedis.connection;

import com.tedis.annotation.SkipIssueCheck;
import com.tedis.common.TedisFuture;

public interface SubCmd<T> {

    /**
     * Redis command: SUBSCRIBE channel [channel ...]
     *
     * @param channels channel to subscribe
     * @return
     */
    @SkipIssueCheck
    TedisFuture<T> subscribe(String... channels);

    /**
     * Redis command: UNSUBSCRIBE [channel [channel ...]]
     *
     * @param channels channels to unsubscribe
     * @return
     */
    @SkipIssueCheck
    TedisFuture<T> unsubscribe(String... channels);

    /**
     * Redis command: PSUBSCRIBE pattern [pattern ...]
     *
     * @param patterns patterns for matching
     * @return
     */
    @SkipIssueCheck
    TedisFuture<T> psubscribe(String... patterns);

    /**
     * Redis command: UNSUBSCRIBE [channel [channel ...]]
     *
     * @param patterns patterns for matching
     * @return
     */
    @SkipIssueCheck
    TedisFuture<T> punsubscribe(String... patterns);

}
