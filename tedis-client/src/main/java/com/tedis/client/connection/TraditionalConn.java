package com.tedis.client.connection;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.ConnPool;
import com.tedis.protocol.Command;
import com.tedis.protocol.Request;
import com.tedis.protocol.Result;
import io.netty.channel.Channel;
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

        // 踩坑点，这里每执行一次此方法都设置一个新的future，多线程情况下暴露问题：
        // 后到的线程覆盖了先前线程的future，导致先前的future永远complete不了，从而导致先前的线程永远阻塞在了future.get()
        // 然而，多线程访问到此方法实际上是一个程序设计上的错误（多线程写一个Socket）
        channel.attr(FUTURE_KEY).set(future);

        channel.attr(RESULT_KEY).set(TRADITIONAL_CONN_RESULT);
        try {
            channel.writeAndFlush(req).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}
