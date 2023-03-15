package com.dempsey.http2nettyserver.ssl;

import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @author Suren Kalaychyan
 */
public class Ssl {

    public SslContext makeSslContext() throws CertificateException, SSLException {
        final SelfSignedCertificate ssc = new SelfSignedCertificate();

        final ApplicationProtocolConfig applicationProtocolConfig = new ApplicationProtocolConfig(
                ApplicationProtocolConfig.Protocol.ALPN,
                ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                ApplicationProtocolNames.HTTP_2);

        final SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                .sslProvider(SslProvider.JDK)
                .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                .applicationProtocolConfig(applicationProtocolConfig)
                .build();

        return sslCtx;
    }
}
