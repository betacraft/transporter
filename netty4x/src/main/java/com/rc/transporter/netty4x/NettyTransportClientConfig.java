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

    /**
     * Getter for @pipeline
     *
     * @return
     */
    public Map<String, ChannelHandler> getPipeline() {
        return pipeline;
    }

    /**
     * Method to add a pipeline handler
     *
     * @param handlerName     name of the handler
     * @param pipelineHandler @ChannelHandler
     * @return
     */
    public NettyTransportClientConfig addPipelineHandler(String handlerName, ChannelHandler pipelineHandler) {
        this.pipeline.put(handlerName, pipelineHandler);
        return this;
    }

    /**
     * Getter for @channelOptions
     *
     * @return
     */
    public Map<ChannelOption, Object> getChannelOptions() {
        return channelOptions;
    }

    /**
     * Setter for @channelOptions
     *
     * @param channelOptions
     */
    public void setChannelOptions(Map<ChannelOption, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    /**
     * Method add a channel option
     *
     * @param channelOption @ChannelOption
     * @param value         value
     */
    public void addChannelOption(ChannelOption channelOption, Object value) {
        if (this.channelOptions == null)
            this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(channelOption, value);
    }

    /**
     * Method to remove a channel option
     *
     * @param channelOption @ChannelOption
     */
    public void removeChannelOption(ChannelOption channelOption) {
        if (this.channelOptions == null)
            return;
        this.channelOptions.remove(channelOption);
    }

    /**
     * Getter for @workerGroup
     *
     * @return
     */
    public NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    /**
     * Setter for @workerGroup
     *
     * @param workerGroup
     */
    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }


    /**
     * Default constructor
     */
    protected NettyTransportClientConfig() {
        this.pipeline = new HashMap<String, ChannelHandler>();
        this.channelOptions = new HashMap<ChannelOption, Object>();
    }

    /**
     * Constructor
     *
     * @param framer   @ChannelHandler for framing incoming/outgoing  data
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     */
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


    /**
     * Constructor
     *
     * @param workerGroup @NioEventLoopGroup for worker
     * @param framer      @ChannelHandler for framing incoming/outgoing  data
     * @param encoder     @ChannelHandler for encoding outgoing data
     * @param decoder     @ChannelHandler for decoding incoming data
     * @param pipeline    Other @ChannelHandler for the pipeline
     */
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


    /**
     * Factory for config with no framer
     *
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     * @return
     */
    public static NettyTransportClientConfig getDefaultWithNoFramer(final ChannelHandler encoder,
                                                                    final ChannelHandler decoder,
                                                                    final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(null, encoder, decoder, pipeline);

    }

    /**
     * Factory
     *
     * @param workerGroup @NioEventLoopGroup for worker
     * @param framer      @ChannelHandler for framing incoming/outgoing  data
     * @param encoder     @ChannelHandler for encoding outgoing data
     * @param decoder     @ChannelHandler for decoding incoming data
     * @param pipeline    Other @ChannelHandler for the pipeline
     * @return
     */
    public static NettyTransportClientConfig getDefault(final NioEventLoopGroup workerGroup,
                                                        final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(workerGroup, framer, encoder, decoder, pipeline);
    }

    /**
     * Factory
     *
     * @param framer   @ChannelHandler for framing incoming/outgoing  data
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     * @return
     */

    public static NettyTransportClientConfig getDefault(final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportClientConfig(framer, encoder, decoder, pipeline);
    }

    /**
     * Factory for clean slate
     *
     * @return
     */
    public static NettyTransportClientConfig get() {
        return new NettyTransportClientConfig();
    }
}
