package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.TransportSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:56 AM
 */
public class NettyTransportClient<M> implements ITransportClient<M> {

    /**
     * Client config
     */
    private NettyTransportClientConfig clientConfig;


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
    public void connect(final String host, final int port, final TransportSession<M> transportSession) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(this.clientConfig.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                        /**
                         * This method will be called once the {@link io.netty.channel.Channel} was registered. After the method returns this instance
                         * will be removed from the {@link io.netty.channel.ChannelPipeline} of the {@link io.netty.channel.Channel}.
                         *
                         * @param ch the {@link io.netty.channel.Channel} which was registered.
                         * @throws Exception is thrown if an error occours. In that case the {@link io.netty.channel.Channel} will be closed.
                         */
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            for (Map.Entry<String, ChannelHandler> pipelineEntry : clientConfig.getPipeline().entrySet()) {
                                pipeline.addLast(pipelineEntry.getKey(), pipelineEntry.getValue());
                            }
                            pipeline.addLast(new NettyTransportSession<M>(transportSession));
                        }
                    });
            // setting all options
            for (Map.Entry<ChannelOption, Object> channelOption : this.clientConfig.getChannelOptions().entrySet()) {
                bootstrap.option(channelOption.getKey(), channelOption.getValue());
            }
            // staring the client
            bootstrap.connect(host, port);
        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Method to close client
     */
    @Override
    public void close() {
        this.clientConfig.getWorkerGroup().shutdownGracefully();
    }
}
