package com.rc.transporter.netty4x;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 2/10/14
 * Time  : 8:12 PM
 */

public class NettyWebsocketTransportSessionTest extends TestCase {


    private static final Logger logger = LoggerFactory.getLogger(NettyWebsocketTransportSessionTest.class);
    private CountDownLatch componentCountdownLatch = new CountDownLatch(1);

    @org.junit.Before
    public void setUp () throws Exception {

    }

    public void testServer () throws Exception {
        /*try {
            NettyTransportServerConfig serverConfig = NettyTransportServerConfig.getDefault(
                    new NettyChannelInitializer() {
                        @Override
                        protected void initializeChannel (ChannelPipeline channelPipeline) {
                            channelPipeline.addLast(new LoggingHandler(LogLevel.TRACE));


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
       // componentCountdownLatch.await();   */
        assertTrue(true);

    }

    @org.junit.After
    public void tearDown () throws Exception {

    }
}
