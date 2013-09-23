package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.TransportSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:56 AM
 */
public class NettyTransportClient<M> implements ITransportClient<M> {


    private NettyTransportClientConfig clientConfig;


    public NettyTransportClient(final NettyTransportClientConfig nettyTransportClientConfig) {
        this.clientConfig = nettyTransportClientConfig;
    }

    @Override
    public void connect(final String host, final int port, final TransportSession<M> transportSession) {
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
                        pipeline.addLast("handler", new NettyTransportSession<M>(transportSession));
                    }
                });
        // staring the client
        bootstrap.connect(host, port);
    }
}
