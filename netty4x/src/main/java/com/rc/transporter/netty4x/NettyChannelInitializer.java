package com.rc.transporter.netty4x;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 12:16 AM
 */
public abstract class NettyChannelInitializer extends ChannelInitializer {
    /**
     * Runtime channel handler provider
     */
    interface RuntimeHandlerProvider {

        ChannelHandler getChannelHandler();
    }

    /**
     * Channel
     */
    private Channel channel;

    /**
     * Runtime channel handler provider
     */
    private RuntimeHandlerProvider runtimeHandlerProvider;


    /**
     * This method will be called once the {@link io.netty.channel.Channel} was registered. After the method returns this instance
     * will be removed from the {@link io.netty.channel.ChannelPipeline} of the {@link io.netty.channel.Channel}.
     *
     * @param channel the {@link io.netty.channel.Channel} which was registered.
     * @throws Exception is thrown if an error occours. In that case the {@link io.netty.channel.Channel} will be closed.
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {
        this.channel = channel;
        initializeChannel(this.channel.pipeline());
        if (this.runtimeHandlerProvider == null)
            return;
        this.channel.pipeline().addLast(this.runtimeHandlerProvider.getChannelHandler());
    }

    protected abstract void initializeChannel(ChannelPipeline channelPipeline);

    void setRuntimeHandlerProvider(final RuntimeHandlerProvider runtimeHandlerProvider) {
        this.runtimeHandlerProvider = runtimeHandlerProvider;
    }
}
