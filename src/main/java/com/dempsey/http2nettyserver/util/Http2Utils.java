package com.dempsey.http2nettyserver.util;

import com.dempsey.http2nettyserver.handler.Http2ServerResponseHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2FrameCodecBuilder;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * @author Suren Kalaychyan
 */
public final class Http2Utils {

    private Http2Utils() {
    }

    public static ApplicationProtocolNegotiationHandler getServerAPNHandler() {
        final ApplicationProtocolNegotiationHandler serverAPNHandler = new ApplicationProtocolNegotiationHandler(ApplicationProtocolNames.HTTP_2) {

            @Override
            protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
                if (ApplicationProtocolNames.HTTP_2.equals(protocol)) {
                    ctx.pipeline().addLast(Http2FrameCodecBuilder.forServer().build(), new Http2ServerResponseHandler());
                    return;
                }
                throw new IllegalStateException("Protocol: " + protocol + " not supported");
            }
        };

        return serverAPNHandler;
    }
}
