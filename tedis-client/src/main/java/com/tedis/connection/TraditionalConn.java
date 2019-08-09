package com.tedis.connection;

import com.tedis.common.Cmd;
import com.tedis.common.TedisFuture;
import com.tedis.pool.ConnPool;
import com.tedis.protocol.Command;
import com.tedis.protocol.Request;
import com.tedis.protocol.Result;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraditionalConn extends AbstractCommonConn<Result> {
    private static Logger log = LoggerFactory.getLogger(TraditionalConn.class);
    public static final AttributeKey<TedisFuture<Result>> FUTURE_KEY = AttributeKey.valueOf("future");

    public TraditionalConn(ConnPool pool, Channel channel) {
        super(pool, channel);
    }

    @Override
    TedisFuture<Result> send(Cmd cmd, String... params) {
        TedisFuture<Result> future = new TedisFuture<>();
        Request<Command> req = new Request<>(generateCmd(cmd, params));

        // 踩坑点，这里每执行一次此方法都设置一个新的future，以下情况暴露问题：
        // 1. 多线程情况
        // 2. 连续写情况
        // 两种情况都可能覆盖先前请求未完成的future，导致前置请求future.get()会阻塞，而后置请求future.get()会得到前置请求的响应
        // 解决：1. 等待上一个future完成再设置新future (request/response)
        //      2. future入列 (pipeline)

        Attribute<TedisFuture<Result>> attr = channel.attr(FUTURE_KEY);
        if (attr.get() != null) {
            attr.get().whenComplete((r, e) -> attr.set(future));
        } else {
            attr.set(future);
        }

        channel.attr(RESULT_KEY).set(TRADITIONAL_CONN_RESULT);
        try {
            channel.writeAndFlush(req).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}
