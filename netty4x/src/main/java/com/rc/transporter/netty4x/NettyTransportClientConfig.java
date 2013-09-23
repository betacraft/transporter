package com.rc.transporter.netty4x;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Netty transport client config
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:03 AM
 */
public class NettyTransportClientConfig {
    /**
     * Worker group
     */
    private NioEventLoopGroup workerGroup;
    /**
     * Channel options
     */
    private Map<ChannelOption, Object> channelOptions;
    /**
     * Pipeline
     */
    private Map<String, ChannelHandler> pipeline;


    public Map<String, ChannelHandler> getPipeline() {
        return pipeline;
    }

    public NettyTransportClientConfig addPipelineHandler(String handlerName, ChannelHandler pipelineHandler) {
        this.pipeline.put(handlerName, pipelineHandler);
        return this;
    }

    public Map<ChannelOption, Object> getChannelOptions() {
        return channelOptions;
    }

    public void setChannelOptions(Map<ChannelOption, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    public void addChannelOption(ChannelOption channelOption, Object value) {
        if (this.channelOptions == null)
            this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(channelOption, value);
    }

    public void removeChannelOption(ChannelOption channelOption) {
        if (this.channelOptions == null)
            return;
        this.channelOptions.remove(channelOption);
    }

    public NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }


    protected NettyTransportClientConfig() {
        this.pipeline = new HashMap<String, ChannelHandler>();
        this.channelOptions = new HashMap<ChannelOption, Object>();
    }

    protected NettyTransportClientConfig(final ChannelHandler framer,
                                         final ChannelHandler encoder,
                                         final ChannelHandler decoder,
                                         final ChannelHandler[] pipeline) {
        this.workerGroup = new NioEventLoopGroup(0);
        this.pipeline = new HashMap<String, ChannelHandler>();
        if (framer != null)
            this.pipeline.put("framer", framer);
        if (decoder != null)
            this.pipeline.put("decoder", decoder);
        if (encoder != null)
            this.pipeline.put("encoder", encoder);
        for (ChannelHandler channelHandler : pipeline)
            this.pipeline.put("handler", channelHandler);
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }

    public NettyTransportClientConfig(final NioEventLoopGroup workerGroup,
                                      final ChannelHandler framer,
                                      final ChannelHandler encoder,
                                      final ChannelHandler decoder,
                                      final ChannelHandler[] pipeline) {
        this.workerGroup = workerGroup;
        this.pipeline = new HashMap<String, ChannelHandler>();
        if (framer != null)
            this.pipeline.put("framer", framer);
        if (decoder != null)
            this.pipeline.put("decoder", decoder);
        if (encoder != null)
            this.pipeline.put("encoder", encoder);
        for (ChannelHandler channelHandler : pipeline)
            this.pipeline.put("handler", channelHandler);
        if (this.pipeline.size() == 0)
            throw new IllegalStateException("Pipeline length is zero");
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }


    public static NettyTransportClientConfig getDefaultWithNoFramer(final ChannelHandler encoder,
                                                                    final ChannelHandler decoder,
                                                                    final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(null, encoder, decoder, pipeline);

    }

    public static NettyTransportClientConfig getDefault(final NioEventLoopGroup workerGroup,
                                                        final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(workerGroup, framer, encoder, decoder, pipeline);
    }


    public static NettyTransportClientConfig getDefault(final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(framer, encoder, decoder, pipeline);
    }

    public static NettyTransportClientConfig get() {
        return new NettyTransportClientConfig();
    }
}
