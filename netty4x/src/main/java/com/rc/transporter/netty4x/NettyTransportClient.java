package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.TransportSession;
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
     * Constructor
     *
     * @param nettyTransportClientConfig @NettyTransportClientConfig
     */
    public NettyTransportClient(final NettyTransportClientConfig nettyTransportClientConfig) {
        this.clientConfig = nettyTransportClientConfig;
    }

    /**
     * Method to make connection
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     * @throws @Exception
     */
    @Override
    public void connect(final String host, final int port, final TransportSession<I, O> transportSession) throws Exception {
        try {
            this.nioEventLoopGroup = this.clientConfig.getNioGroupFactory().get();
            this.clientConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {
                @Override
                public void appendRuntimeHandler(final ChannelPipeline pipeline) {
                    if (clientConfig.getSessionEventsExecutorFactory() == null) {
                        logger.warn("No session events executor factory assigned\nMake sure you are not stealing time from worker group for better performance");
                        pipeline.addLast(new NettyTransportSession<I, O>(transportSession));
                    } else {
                        executorGroup = clientConfig.getSessionEventsExecutorFactory().get();
                        pipeline.addLast(executorGroup, new NettyTransportSession<I, O>(transportSession));
                    }
                }
            });
            Bootstrap bootstrap = new Bootstrap()
                    .group(this.nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(this.clientConfig.getChannelInitializer());
            // setting all options
            for (Map.Entry<ChannelOption, Object> channelOption : this.clientConfig.getChannelOptions().entrySet()) {
                bootstrap.option(channelOption.getKey(), channelOption.getValue());
            }
            // staring the client
            bootstrap.connect(host, port).channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    close();
                }
            });


        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Method to close client
     */
    @Override
    public void close() {
        logger.debug("Transport client is closed");
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
