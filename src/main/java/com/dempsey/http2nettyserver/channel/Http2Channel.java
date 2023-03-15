package com.dempsey.http2nettyserver.channel;

import com.dempsey.http2nettyserver.util.Http2Utils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

/**
 * @author Suren Kalaychyan
 */
public class Http2Channel extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public Http2Channel(final SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        if (sslCtx != null) {
            ch.pipeline()
                    .addLast(sslCtx.newHandler(ch.alloc()), Http2Utils.getServerAPNHandler());
        }
    }
}
