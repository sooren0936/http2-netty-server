package com.dempsey.http2nettyserver;

import com.dempsey.http2nettyserver.server.Http2Server;

/**
 * @author Suren Kalaychyan
 */
public class Http2NettyServerApp {

    public static void main(final String[] args) throws Exception {
        final Http2Server http2Server = new Http2Server();
        http2Server.init();
    }
}
