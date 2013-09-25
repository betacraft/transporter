package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.TransportSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:56 AM
 */
public class NettyTransportClient<I, O> implements ITransportClient<I, O> {

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
    public void connect(final String host, final int port, final TransportSession<I, O> transportSession) throws Exception {
        try {
            this.clientConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {
                @Override
                public ChannelHandler getChannelHandler() {
                    return new NettyTransportSession<I, O>(transportSession);
                }
            });
            Bootstrap bootstrap = new Bootstrap()
                    .group(this.clientConfig.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .handler(this.clientConfig.getChannelInitializer());
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
