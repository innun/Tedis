package com.tedis.proxy;

import com.tedis.TedisClient;
import com.tedis.annotation.SkipIssueCheck;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;

public class TedisInvocationHandler implements InvocationHandler {
    private TedisClient tedisClient;
    private static HashSet<Method> set;

    public TedisInvocationHandler(TedisClient tedisClient) {
        this.tedisClient = tedisClient;
        set = new HashSet<>();
        try {
            set.add(TedisClient.class.getDeclaredMethod("subscribe", String[].class));
            set.add(TedisClient.class.getDeclaredMethod("psubscribe", String[].class));
            set.add(TedisClient.class.getDeclaredMethod("unsubscribe", String[].class));
            set.add(TedisClient.class.getDeclaredMethod("punsubscribe", String[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getAnnotation(SkipIssueCheck.class) == null &&
                tedisClient.getMode() == TedisClient.SUB &&
                !set.contains(method)) {
            throw new RuntimeException("Not supposed to issue any other commands in subscribe mode");
        }
        return method.invoke(tedisClient, args);
    }
}
