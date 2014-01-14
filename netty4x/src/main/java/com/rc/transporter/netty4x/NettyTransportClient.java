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
    private ITransportOutgoingSession underlyingSession;
    /**
     * Outgoing session associated with current connection
     */
    private NettyOutgoingTransportSession nettyOutgoingTransportSession;
    /**
     * Bootstrap for client
     */
    private Bootstrap bootstrap;


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
            this.underlyingSession = transportSession;
            this.nioEventLoopGroup = this.clientConfig.getWorkerGroupFactory().get();
            connectInternal(host, port);
        } catch (Exception exception) {
            logger.error("Exception while connection to {}:{}", host, port, exception);
            throw exception;
        }

    }


    private void connectInternal (final String host, final int port) {
        logger.debug("Connecting with recovery logic");
        this.nettyOutgoingTransportSession = new NettyOutgoingTransportSession(this.underlyingSession,
                new NettyOutgoingChannelStateListener() {
                    @Override
                    public void onSessionDropped () {
                        logger.info("Session dropped");
                    }

                    @Override
                    public void onSessionDropped (Throwable cause) {
                        logger.error("On session dropped", cause);
                    }
                }, retries != 0);
        this.clientConfig.getChannelInitializer()
                .setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {
                    @Override
                    public void appendRuntimeHandler (final ChannelPipeline pipeline) {
                        if (clientConfig.getSessionEventsExecutorFactory() == null) {
                            logger.warn("No session events executor factory assigned" +
                                    "\nMake sure you are not stealing time from worker group for " +
                                    "better performance");
                            pipeline.addLast(nettyOutgoingTransportSession);
                        } else {
                            executorGroup = clientConfig.getSessionEventsExecutorFactory().get();
                            pipeline.addLast(executorGroup, nettyOutgoingTransportSession);
                        }
                    }
                });
        if (this.bootstrap == null) {
            this.bootstrap = new Bootstrap()
                    .group(this.nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(this.clientConfig.getChannelInitializer());
            // setting all options
            for (Map.Entry<ChannelOption, Object> channelOption : this.clientConfig.getChannelOptions()
                    .entrySet()) {
                bootstrap.option(channelOption.getKey(), channelOption.getValue());
            }
        }
        // staring the client
        this.bootstrap.connect(host, port).channel().closeFuture()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete (ChannelFuture future) throws Exception {
                        logger.info("Client bootstrap closed");
                        if (++retries < clientConfig.getAutoRecoverAttempts() && !isClosed.get()
                                && clientConfig.getAutoRecover()) {
                            connectInternal(host, port);
                        } else {
                            if (underlyingSession != null)
                                underlyingSession.onDisconnected();
                            close();
                        }
                    }
                });
    }

    /**
     * Method to close client
     */
    @Override
    public void close () {
        logger.debug("Transport client is closed");
        this.isClosed.set(true);
        if (this.nettyOutgoingTransportSession != null) {
            this.nettyOutgoingTransportSession.closeSession();
        }
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
