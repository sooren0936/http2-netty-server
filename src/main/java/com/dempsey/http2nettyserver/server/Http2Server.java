package com.dempsey.http2nettyserver.server;

import com.dempsey.http2nettyserver.channel.Http2Channel;
import com.dempsey.http2nettyserver.ssl.Ssl;
import com.dempsey.http2nettyserver.util.PropertyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Suren Kalaychyan
 */
public final class Http2Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Http2Server.class);

    private int port;

    public Http2Server() {
        final String propertyPort = PropertyUtils.findProperty("port");
        this.port = Integer.parseInt(propertyPort);
    }

    public void init() throws Exception {
        final Ssl ssl = new Ssl();
        final SslContext sslCtx = ssl.makeSslContext();

        final Http2Channel http2Channel = new Http2Channel(sslCtx);
        final EventLoopGroup group = new NioEventLoopGroup();

        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(http2Channel);
            final Channel ch = bootstrap.bind(port).sync().channel();

            LOGGER.info("HTTP/2 Server is listening on https://127.0.0.1:{}/", port);

            ch.closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}