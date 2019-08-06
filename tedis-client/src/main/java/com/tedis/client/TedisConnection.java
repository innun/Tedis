package com.tedis.client;

import com.tedis.client.common.Cmd;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.TedisPool;
import com.tedis.protocol.Command;
import com.tedis.protocol.Request;
import com.tedis.protocol.Result;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TedisConnection extends AbstractConnection<Result> {
    private static Logger log = LoggerFactory.getLogger(TedisConnection.class);
    public static final AttributeKey<TedisFuture<Result>> FUTURE_KEY = AttributeKey.valueOf("future");


    public TedisConnection(Channel channel, TedisPool pool) {
        super(channel, pool);
    }

    @Override
    TedisFuture<Result> execute(Cmd cmd, String... params) {
        TedisFuture<Result> future;
        Request<Command> req = new Request<>(generateCmd(cmd, params));

        // 踩坑点，之前的做法是每执行一次此方法都设置一个新的future，多线程情况下暴露问题：
        // 后到的线程覆盖了先前线程的future，导致先前的future永远complete不了，从而导致先前的线程永远阻塞在了future.get()
        if ((future = channel.attr(FUTURE_KEY).get()) == null) {
            future = new TedisFuture<>();
            channel.attr(FUTURE_KEY).set(future);
        }

        channel.attr(RESULT_NUM_KEY).set(-1);
        try {
            channel.writeAndFlush(req).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}
