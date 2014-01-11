package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.ITransportOutgoingSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:56 AM
 */
public class NettyTransportClient<I, O> implements ITransportClient<I, O> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportClient.class);
    /**
     * Client config
     */
    private NettyTransportClientConfig clientConfig;
    /**
     * Current executor loop group
     */
    private EventExecutorGroup executorGroup;
    /**
     * Current loop group
     */
    private NioEventLoopGroup nioEventLoopGroup;
    /**
     * is closed flag
     */
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    /**
     * Retry attempts made so far
     */
    private int retries = 0;
    /**
     * Underlying session
     */
    private ITransportOutgoingSession undelyingSession;
    /**
     * Outgoing session associated with current connection
     */
    private NettyOutgoingTransportSession nettyOutgoingTransportSession;
    /**
     * Bootstrap for client
     */
    private Bootstrap bootstrap;
    /**
     * Handler name
     */
    private final static String HANDLER_NAME = "transporter_handler";

    /**
     * Constructor
     *
     * @param nettyTransportClientConfig @NettyTransportClientConfig
     */
    public NettyTransportClient (final NettyTransportClientConfig nettyTransportClientConfig) {
        this.clientConfig = nettyTransportClientConfig;
    }

    /**
     * Method to make connection
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportIncomingSession to listen to the events associated wtih session
     * @throws @Exception
     */
    @Override
    public void connect (final String host, final int port,
            final ITransportOutgoingSession<I, O> transportSession) throws Exception {
        try {
            this.undelyingSession = transportSession;
            this.nioEventLoopGroup = this.clientConfig.getWorkerGroupFactory().get();
            if (this.clientConfig.getAutoRecover()) {
                connectWithRecoveryLogic(host, port);
            } else {

            }
        } catch (Exception exception) {
            logger.error("Exception while connection to {}:{}", host, port, exception);
            throw exception;
        }

    }

    private void connectWithRecoveryLogic (final String host, final int port) {
        while (retries < clientConfig.getAutoRecoverAttempts()) {
            this.nettyOutgoingTransportSession = new NettyOutgoingTransportSession(this.undelyingSession,
                    new NettyOutgoingChannelStateListener() {
                        @Override
                        public void onSessionDropped () {

                        }

                        @Override
                        public void onSessionDropped (Throwable cause) {
                            logger.error("On session dropped", cause);
                        }
                    }, retries == 0);
            this.clientConfig.getChannelInitializer()
                    .setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {
                        @Override
                        public void appendRuntimeHandler (final ChannelPipeline pipeline) {
                            try {
                                pipeline.remove(HANDLER_NAME);
                            } catch (NoSuchElementException ignored) {
                            }
                            if (clientConfig.getSessionEventsExecutorFactory() == null) {
                                logger.warn("No session events executor factory assigned" +
                                        "\nMake sure you are not stealing time from worker group for " +
                                        "better performance");
                                pipeline.addLast(HANDLER_NAME, nettyOutgoingTransportSession);
                            } else {
                                executorGroup = clientConfig.getSessionEventsExecutorFactory().get();
                                pipeline.addLast(executorGroup, HANDLER_NAME, nettyOutgoingTransportSession);
                            }
                        }
                    });
            this.bootstrap = new Bootstrap()
                    .group(this.nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(this.clientConfig.getChannelInitializer());
            // setting all options
            for (Map.Entry<ChannelOption, Object> channelOption : this.clientConfig.getChannelOptions()
                    .entrySet()) {
                bootstrap.option(channelOption.getKey(), channelOption.getValue());
            }
            // staring the client
            bootstrap.connect(host, port).channel().closeFuture()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete (ChannelFuture future) throws Exception {
                            logger.info("Client bootstrap closed");
                            if (++retries > clientConfig.getAutoRecoverAttempts() || isClosed.get()) {
                                if (undelyingSession != null)
                                    undelyingSession.onDisconnected();
                            }
                        }
                    });
            this.undelyingSession.onRecoveryStarted();
        }
    }

    /**
     * Method to close client
     */
    @Override
    public void close () {
        logger.debug("Transport client is closed");
        this.isClosed.set(true);
        if (!this.clientConfig.getKeepExecutorGroupAlive() && this.executorGroup != null) {
            logger.debug("Shutting down executor group");
            this.executorGroup.shutdownGracefully();
        }
        if (!this.clientConfig.getKeepNioGroupAlive()) {
            logger.debug("Shutting down nio group");
            this.nioEventLoopGroup.shutdownGracefully();
        }
    }
}
