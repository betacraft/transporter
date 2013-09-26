package com.rc.transporter.netty3x;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * Netty 3x channel pipeline factory
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 3:29 AM
 */
public abstract class Netty3xChannelPipelineFactory implements ChannelPipelineFactory {

    /**
     * Runtime channel handler provider which is required at the time of creation
     */
    interface RuntimeChannelHandlerProvider {
        ChannelHandler get();
    }

    /**
     * @RuntimeChannelHandlerProvider associated with this factory
     */
    private RuntimeChannelHandlerProvider runtimeChannelHandlerProvider;

    /**
     * Returns a newly created {@link org.jboss.netty.channel.ChannelPipeline}.
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        initializeChannel(pipeline);
        if (runtimeChannelHandlerProvider == null)
            return pipeline;
        pipeline.addLast("netty_handler", this.runtimeChannelHandlerProvider.get());
        return pipeline;
    }

    /**
     * Method to get pipeline initialized
     *
     * @param pipeline @ChannelPipeline associated with this
     */
    public abstract void initializeChannel(final ChannelPipeline pipeline);


    /**
     * Setter for @runtimeChannelHandlerProvider
     *
     * @param runtimeChannelHandlerProvider @RuntimeChannelHandlerProvider
     */
    void setRuntimeChannelHandlerProvider(final RuntimeChannelHandlerProvider runtimeChannelHandlerProvider) {
        this.runtimeChannelHandlerProvider = runtimeChannelHandlerProvider;
    }

}
