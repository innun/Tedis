package com.tedis.api;

import com.tedis.exception.ConnectFailException;
import com.tedis.exception.IllegalLockOperation;

import java.io.IOException;

public interface Lock {
    void lock() throws ConnectFailException, InterruptedException;

    void unlock() throws IllegalLockOperation, IOException;
}
