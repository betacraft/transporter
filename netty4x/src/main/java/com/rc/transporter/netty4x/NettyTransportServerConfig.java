package com.rc.transporter.netty4x;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:03 AM
 */
public final class NettyTransportServerConfig extends NettyTransportClientConfig {
    /**
     * Boss group
     */
    private NioEventLoopGroupFactory bossGroupFactory;
    /**
     * Child options
     */
    private Map<ChannelOption, Object> childChannelOptions;
    /**
     * keep nio event loop group alive
     */
    private AtomicBoolean keepBossGroupAlive = new AtomicBoolean(false);

    /**
     * Getter for @keepBossGroupAlive
     *
     * @return
     */
    public boolean getKeepBossGroupAlive() {
        return keepBossGroupAlive.get();
    }

    /**
     * Setter for @keepBossGroupAlive
     *
     * @param keepBossGroupAlive
     */
    public void setKeepBossGroupAlive(final boolean keepBossGroupAlive) {
        this.keepBossGroupAlive.set(keepBossGroupAlive);
    }

    /**
     * Getter for @bossGroupFactory
     *
     * @return
     */
    public NioEventLoopGroupFactory getBossGroupFactory() {
        return bossGroupFactory;
    }

    /**
     * Setter for @bossGroup
     *
     * @param bossGroupFactory
     */
    public void setBossGroupFactory(NioEventLoopGroupFactory bossGroupFactory) {
        this.bossGroupFactory = bossGroupFactory;
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
     * @param channelInitializer @ChannelInitializer for connections
     */
    private NettyTransportServerConfig(final NettyChannelInitializer channelInitializer) {
        super(channelInitializer);
        this.bossGroupFactory = new NioEventLoopGroupFactory() {
            @Override
            public NioEventLoopGroup get() {
                return new NioEventLoopGroup(0);
            }
        };
        // Add so backlogs for servers

        addChannelOption(ChannelOption.SO_BACKLOG, 100);
        addChannelOption(ChannelOption.SO_KEEPALIVE, true);
        addChannelOption(ChannelOption.TCP_NODELAY, true);

        this.childChannelOptions = new HashMap<ChannelOption, Object>();
        this.childChannelOptions.put(ChannelOption.ALLOW_HALF_CLOSURE, true);
        this.childChannelOptions.put(ChannelOption.SO_SNDBUF, 1048576);
        this.childChannelOptions.put(ChannelOption.SO_RCVBUF, 1048576);
        this.childChannelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.childChannelOptions.put(ChannelOption.SO_KEEPALIVE, true);

    }


    /**
     * Get transport server config with specified framer
     *
     * @param channelInitializer @ChannelInitializer for connections
     * @return instance of @NettyTransportServerConfig
     */
    public static NettyTransportServerConfig getDefault(final NettyChannelInitializer channelInitializer) {
        return new NettyTransportServerConfig(channelInitializer);
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
