package com.tedis.proxy;

import com.tedis.TedisClient;
import com.tedis.annotation.SkipIssueCheck;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TedisInvocationHandler implements InvocationHandler {
    private TedisClient tedisClient;

    public TedisInvocationHandler(TedisClient tedisClient) {
        this.tedisClient = tedisClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getAnnotation(SkipIssueCheck.class) == null &&
                tedisClient.getMode() == TedisClient.SUB) {
            throw new RuntimeException("Not supposed to issue any other commands in subscribe mode");
        }
        return method.invoke(tedisClient, args);
    }
}
