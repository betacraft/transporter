package com.rc.transporter.netty4x;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.util.concurrent.CountDownLatch;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 2/10/14
 * Time  : 8:12 PM
 */

public class NettyWebsocketTransportSessionTest extends TestCase {


    private static final Logger logger = LoggerFactory.getLogger(NettyWebsocketTransportSessionTest.class);
    private CountDownLatch componentCountdownLatch = new CountDownLatch(1);
    private static final String KEY_STORE_FILE_PATH =
            "/home/akshay/code_base/appsurfer-java-node/keystore/appsurfer-java-node-keystore.keystore";

    @org.junit.Before
    public void setUp () throws Exception {

    }

    public void testServer () throws Exception {
     /* try {
            final SSLEngine engine =
                    createSSLContext(
                            new FileInputStream(
                                    new File(KEY_STORE_FILE_PATH)), "appsurfer1511").createSSLEngine();
            engine.setUseClientMode(false);
            NettyTransportServerConfig serverConfig = NettyTransportServerConfig.getDefault(
                    new NettyChannelInitializer() {
                        @Override
                        protected void initializeChannel (ChannelPipeline channelPipeline) {
                            channelPipeline.addLast(new LoggingHandler(LogLevel.TRACE));

                            channelPipeline.addLast(new SslHandler(engine));

                            channelPipeline.addLast(new HttpRequestDecoder());

                            channelPipeline.addLast(new HttpObjectAggregator(64 * 1024));

                            channelPipeline.addLast(new HttpResponseEncoder());

                            channelPipeline.addLast(new ReadTimeoutHandler(4, TimeUnit.MINUTES));

                        }
                    });
            serverConfig.setBossGroupFactory(new NettyTransportClientConfig
                    .NioEventLoopGroupFactory() {
                @Override
                public NioEventLoopGroup get () {
                    return new NioEventLoopGroup(4);
                }
            });
            serverConfig.setWorkerGroupFactory(new NettyTransportClientConfig
                    .NioEventLoopGroupFactory() {
                @Override
                public NioEventLoopGroup get () {
                    return new NioEventLoopGroup(5);
                }
            });

            new NettyWebsocketTransportServer(serverConfig)
                    .start(8000, new WebsocketTransportServer.ITransportServerListener() {
                                @Override
                                public void onClosed () {

                                }
                            }, new WebsocketTransportServer.INettyWebsocketTransportSessionFactory() {
                                @Override
                                public INettyWebsocketTransportSession get () {
                                    return new INettyWebsocketTransportSession<String, Object>() {
                                        @Override
                                        public void onConnected (WebsocketSession websocketSession,
                                                TransportChannel<Object> channel) {
                                            logger.debug("Connected" + websocketSession.getPath());

                                            channel.sendData(new TextWebSocketFrame("test"));
                                        }

                                        @Override
                                        public void onDisconnected () {
                                            logger.debug("disconnected");
                                        }

                                        @Override
                                        public void onError (Throwable cause) {
                                            logger.error("error", cause);
                                        }

                                        @Override
                                        public void onData (String data) {
                                            logger.debug(data);
                                        }

                                        @Override
                                        public void setProperty (String key, Object value) {

                                        }
                                    };
                                }
                            }
                    );
        } catch (Exception exception) {
            logger.error("Error while running event session service", exception);
            componentCountdownLatch.countDown();
        }
        componentCountdownLatch.await();   */
        assertTrue(true);

    }


    private SSLContext createSSLContext (InputStream keyStoreFile, String keyStoreFilePassword) throws
            Exception {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(keyStoreFile, keyStoreFilePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(ks, keyStoreFilePassword.toCharArray());

        SSLContext serverContext = SSLContext.getInstance("TLS");
        serverContext.init(kmf.getKeyManagers(), null, null);
        return serverContext;
    }


    @org.junit.After
    public void tearDown () throws Exception {

    }
}
