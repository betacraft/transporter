package com.rc.transporter.netty4x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Author: akshay
 * Date  : 1/13/14
 * Time  : 4:26 AM
 */
public class NettyTransportClientTest {
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportClientTest.class);
    private CountDownLatch waitForServer = new CountDownLatch(1);

    @org.junit.Before
    public void setUp () throws Exception {
      /*  new Thread(new Runnable() {
            @Override
            public void run () {
                final ScheduledExecutorService tester = Executors.newSingleThreadScheduledExecutor();
                try {
                    logger.debug("Starting event server on " + 9000);
                    NettyTransportServerConfig serverConfig = NettyTransportServerConfig.getDefault(
                            new NettyChannelInitializer() {
                                @Override
                                protected void initializeChannel (ChannelPipeline channelPipeline) {

                                    channelPipeline.addLast(new DelimiterBasedFrameDecoder(16777216,
                                            Delimiters.lineDelimiter()));

                                    channelPipeline.addLast(new StringDecoder());

                                    channelPipeline.addLast(new StringEncoder());

                                    channelPipeline.addLast(new ReadTimeoutHandler(4, TimeUnit.MINUTES));

                                }
                            });
                    serverConfig.setBossGroupFactory(new NettyTransportClientConfig
                            .NioEventLoopGroupFactory() {
                        @Override
                        public NioEventLoopGroup get () {
                            return new NioEventLoopGroup(1);
                        }
                    });
                    serverConfig.setWorkerGroupFactory(new NettyTransportClientConfig
                            .NioEventLoopGroupFactory() {
                        @Override
                        public NioEventLoopGroup get () {
                            return new NioEventLoopGroup(1);
                        }
                    });
                    new NettyTransportServer(serverConfig)
                            .start(9000, new TransportServer.ITransportServerListener() {
                                        @Override
                                        public void onClosed () {
                                        }
                                    }, new TransportServer.ITransportIncomingSessionFactory() {
                                        @Override
                                        public ITransportIncomingSession<String, String> get () {
                                            return new ITransportIncomingSession<String, String>() {
                                                @Override
                                                public void onConnected (final ITransportChannel channel) {
                                                    logger.debug("connected from server");
                                                    tester.schedule(new Runnable() {
                                                        @Override
                                                        public void run () {
                                                            channel.close();
                                                        }
                                                    }, 10, TimeUnit.SECONDS);
                                                    channel.sendData("test\n");
                                                }

                                                @Override
                                                public void onDisconnected () {
                                                    logger.debug("disconnected from server");

                                                }

                                                @Override
                                                public void onError (Throwable cause) {
                                                    logger.debug("on error from server", cause);
                                                }

                                                @Override
                                                public void onData (String data) {
                                                    logger.debug("on data from server" + data);
                                                }

                                                @Override
                                                public void setProperty (String key, Object value) {
                                                    //To change body of implemented methods use File |
                                                    // Settings | File Templates.
                                                }
                                            };
                                        }
                                    }
                            );
                } catch (Exception exception) {
                    logger.error("Error while running event session service", exception);

                }
            }
        }).start();  */
    }


   /* @Test
    public void testAutoRecover () throws Exception {
        Thread.sleep(10000);
        final ScheduledExecutorService tester = Executors.newSingleThreadScheduledExecutor();
        CountDownLatch test = new CountDownLatch(1);
        NettyTransportClientConfig clientConfig = NettyTransportClientConfig
                .getDefault(new NettyTransportClientConfig.NioEventLoopGroupFactory() {
                                @Override
                                public NioEventLoopGroup get () {
                                    return new NioEventLoopGroup();
                                }
                            }, new NettyChannelInitializer() {
                                @Override
                                protected void initializeChannel (ChannelPipeline channelPipeline) {
                                    //channelPipeline.addFirst(new LoggingHandler(LogLevel.DEBUG));
                                    channelPipeline.addLast(new DelimiterBasedFrameDecoder(16777216,
                                            Delimiters.lineDelimiter()));

                                    channelPipeline.addLast(new StringDecoder());

                                    channelPipeline.addLast(new StringEncoder());

                                }
                            }
                );
        clientConfig.setAutoRecoverAttempts(3);
        clientConfig.setAutoRecover(true);
        clientConfig.setKeepNioGroupAlive(true);

        NettyTransportClient client = new NettyTransportClient<String, String>(clientConfig);
        client.connect("127.0.0.1", 9000, new ITransportOutgoingSession<String, String>() {
            @Override
            public void onRecoveryStarted () {
                logger.debug("recovery started from client");
            }

            @Override
            public void onRecovered (final ITransportChannel channel) {
                logger.debug("recovered from client");
                channel.sendData("test\n");
            }

            @Override
            public void onRecoveryFailed () {
                logger.debug("recovery failed from client");
            }

            @Override
            public void onConnected (final ITransportChannel channel) {
                logger.debug("connected from client");
            }

            @Override
            public void onDisconnected () {
                logger.debug("disconnected from client");
            }

            @Override
            public void onError (Throwable cause) {
                logger.debug("error from client", cause);
            }

            @Override
            public void onData (String data) {
                logger.debug("on data " + data);
            }

            @Override
            public void setProperty (String key, Object value) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        /*EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChannelInboundHandler<Object>() {

                        @Override
                        public void channelInactive (ChannelHandlerContext ctx) throws Exception {
                            super.channelInactive(ctx);    //To change body of overridden methods use File
                            System.out.println("channel is inactive client");
                        }

                        @Override
                        protected void channelRead0 (ChannelHandlerContext ctx, Object msg) throws Exception {

                        }
                    });

            // Make the connection attempt.
            ChannelFuture f = b.connect("localhost", 9000).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }



    }  */

    @org.junit.After
    public void tearDown () throws Exception {

    }
}
