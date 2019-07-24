package com.tedis.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class TedisClient implements Client {
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private String host;
    private int port;


    private TedisClient() {
        init();
    }

    private void init() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }

    public static TedisClient create(TedisConfig config) {
        TedisClient instance = new TedisClient();
        instance.host = config.getHost();
        instance.port = config.getPort();
        return instance;
    }


    @Override
    public Connection connect() throws InterruptedException {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new TedisChannelInitializer());
        // TODO: SET TCP options needed
//                .option(, );
        ChannelFuture f = bootstrap.connect(host, port).sync();
        return new TedisConnection(f.channel());
    }
}
