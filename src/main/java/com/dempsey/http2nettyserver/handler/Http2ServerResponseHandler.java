package com.dempsey.http2nettyserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.*;
import io.netty.util.CharsetUtil;

/**
 * @author Suren Kalaychyan
 */
public class Http2ServerResponseHandler extends ChannelDuplexHandler {

    private static final ByteBuf RESPONSE_BYTES = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("Message", CharsetUtil.UTF_8));

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof Http2HeadersFrame msgHeader) {
            if (msgHeader.isEndStream()) {
                final ByteBuf content = ctx.alloc().buffer();
                content.writeBytes(RESPONSE_BYTES.duplicate());

                final Http2Headers headers = new DefaultHttp2Headers()
                        .status(HttpResponseStatus.OK.codeAsText());

                ctx.write(new DefaultHttp2HeadersFrame(headers)
                        .stream(msgHeader.stream()));
                ctx.write(new DefaultHttp2DataFrame(content, true)
                        .stream(msgHeader.stream()));
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
