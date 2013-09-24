package com.rc.transporter.netty4x;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:03 AM
 */
public final class NettyTransportServerConfig extends NettyTransportClientConfig {
    /**
     * Boss group
     */
    private NioEventLoopGroup bossGroup;
    /**
     * Child options
     */
    private Map<ChannelOption, Object> childChannelOptions;

    /**
     * Getter for @bossGroup
     *
     * @return
     */
    public NioEventLoopGroup getBossGroup() {
        return bossGroup;
    }

    /**
     * Setter for @bossGroup
     *
     * @param bossGroup
     */
    public void setBossGroup(NioEventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }


    /**
     * Getter for @childChannelOptions
     *
     * @return
     */
    public Map<ChannelOption, Object> getChildChannelOptions() {
        return childChannelOptions;
    }

    /**
     * Setter for @childChannelOptions
     *
     * @param childChannelOptions
     */
    public void setChildChannelOptions(Map<ChannelOption, Object> childChannelOptions) {
        this.childChannelOptions = childChannelOptions;
    }

    /**
     * Method to add a child channel option
     *
     * @param channelOption @ChannelOption
     * @param value         value
     */
    public void addChildChannelOption(ChannelOption channelOption, Object value) {
        if (this.childChannelOptions == null)
            this.childChannelOptions = new HashMap<ChannelOption, Object>();
        this.childChannelOptions.put(channelOption, value);
    }

    /**
     * Method to remove a child channel option
     *
     * @param channelOption @ChannelOption
     */
    public void removeChildChannelOption(ChannelOption channelOption) {
        if (this.childChannelOptions == null)
            return;
        this.childChannelOptions.remove(channelOption);
    }


    /**
     * Default constructor
     */
    private NettyTransportServerConfig() {
        super();
        this.childChannelOptions = new HashMap<ChannelOption, Object>();
    }

    /**
     * Constructor
     *
     * @param framer   @ChannelHandler for framing incoming/outgoing  data
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     */
    private NettyTransportServerConfig(final ChannelHandler framer,
                                       final ChannelHandler encoder,
                                       final ChannelHandler decoder,
                                       final ChannelHandler... pipeline) {
        super(framer, encoder, decoder, pipeline);
        this.bossGroup = new NioEventLoopGroup(0);
        // Add so backlogs for servers
        addChannelOption(ChannelOption.SO_BACKLOG, 100);
        this.childChannelOptions = new HashMap<ChannelOption, Object>();
        this.childChannelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.childChannelOptions.put(ChannelOption.SO_KEEPALIVE, true);

    }

    /**
     * Get transport server config which does not have any framer
     *
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     * @return instance of @NettyTransportServerConfig
     */
    public static NettyTransportServerConfig getDefaultWithNoFramer(
            final ChannelHandler encoder,
            final ChannelHandler decoder,
            final ChannelHandler... pipeline) {
        return new NettyTransportServerConfig(null, encoder, decoder, pipeline);
    }

    /**
     * Get transport server config with specified framer
     *
     * @param framer   @ChannelHandler for framing incoming/outgoing  data
     * @param encoder  @ChannelHandler for encoding outgoing data
     * @param decoder  @ChannelHandler for decoding incoming data
     * @param pipeline Other @ChannelHandler for the pipeline
     * @return instance of @NettyTransportServerConfig
     */
    public static NettyTransportServerConfig getDefault(final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportServerConfig(framer, encoder, decoder, pipeline);
    }

    /**
     * Get clean slate instance
     *
     * @return instance of @NettyTransportServerConfig
     */
    public static NettyTransportServerConfig get() {
        return new NettyTransportServerConfig();
    }
}
