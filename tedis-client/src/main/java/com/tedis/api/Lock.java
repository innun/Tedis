package com.tedis.api;

import com.tedis.client.exception.ConnectFailException;
import com.tedis.client.exception.IllegalLockOperation;

public interface Lock {
    void lock() throws ConnectFailException, InterruptedException;

    void unlock() throws IllegalLockOperation;
}
