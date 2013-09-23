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


    public NioEventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(NioEventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }


    public Map<ChannelOption, Object> getChildChannelOptions() {
        return childChannelOptions;
    }

    public void setChildChannelOptions(Map<ChannelOption, Object> childChannelOptions) {
        this.childChannelOptions = childChannelOptions;
    }

    public void addChildChannelOption(ChannelOption channelOption, Object value) {
        if (this.childChannelOptions == null)
            this.childChannelOptions = new HashMap<ChannelOption, Object>();
        this.childChannelOptions.put(channelOption, value);
    }

    public void removeChildChannelOption(ChannelOption channelOption) {
        if (this.childChannelOptions == null)
            return;
        this.childChannelOptions.remove(channelOption);
    }


    private NettyTransportServerConfig() {
        super();
        this.childChannelOptions = new HashMap<ChannelOption, Object>();
    }

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

    private NettyTransportServerConfig(final NioEventLoopGroup bossGroup,
                                       final NioEventLoopGroup workerGroup,
                                       final ChannelHandler encoder,
                                       final ChannelHandler decoder,
                                       final ChannelHandler... pipeline) {


    }


    public static NettyTransportServerConfig getDefaultWithNoFramer(
            final ChannelHandler encoder,
            final ChannelHandler decoder,
            final ChannelHandler... pipeline) {
        return new NettyTransportServerConfig(null, encoder, decoder, pipeline);
    }


    public static NettyTransportServerConfig getDefault(final ChannelHandler framer,
                                                        final ChannelHandler encoder,
                                                        final ChannelHandler decoder,
                                                        final ChannelHandler... pipeline) {
        return new NettyTransportServerConfig(framer, encoder, decoder, pipeline);
    }

    public static NettyTransportServerConfig get() {
        return new NettyTransportServerConfig();
    }
}
