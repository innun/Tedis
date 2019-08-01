package com.tedis.client;

import com.tedis.api.Client;
import com.tedis.client.exception.ConnectFailException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TedisClient implements Client {

    private static Logger log = LoggerFactory.getLogger(TedisClient.class);

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private Channel channel;
    private String host;
    private int port;
    private String password;


    private TedisClient() {
        init();
    }

    private void init() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new TedisChannelInitializer())
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public static TedisClient create(TedisClientConfig config) {
        TedisClient instance = new TedisClient();
        instance.host = config.getHost();
        instance.port = config.getPort();
        instance.password = config.getPassword();
        return instance;
    }



    @Override
    public Channel connect() throws ConnectFailException {
        TedisConnection conn;
        ChannelFuture f;
        try {
            f = bootstrap.connect(host, port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        conn = new TedisConnection(channel);
        if (!conn.auth(password).sync().getResult().equals("\"OK\"")) {
            throw new ConnectFailException("invalid password");
        }
        return channel;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        channel.close();
        log.info("connection => {} now closed", channel);
    }
}
