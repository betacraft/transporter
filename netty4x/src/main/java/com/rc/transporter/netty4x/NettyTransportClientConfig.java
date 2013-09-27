package com.rc.transporter.netty4x;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;

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
     * Channel initializer
     */
    private NettyChannelInitializer channelInitializer;
    /**
     * Session Event executors
     */
    private EventExecutorGroup sessionEventsExecutor;


    /**
     * Getter for @sessionEventsExecutor
     *
     * @return
     */
    public EventExecutorGroup getSessionEventsExecutor() {
        return sessionEventsExecutor;
    }

    /**
     * Setter for @sessionEventsExecutor
     *
     * @param sessionEventsExecutor
     */
    public void setSessionEventsExecutor(EventExecutorGroup sessionEventsExecutor) {
        this.sessionEventsExecutor = sessionEventsExecutor;
    }

    /**
     * Getter for @channelInitializer
     *
     * @return
     */
    public NettyChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    /**
     * Setter for @channelInitializer
     *
     * @param channelInitializer
     */
    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
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

        this.channelOptions = new HashMap<ChannelOption, Object>();
    }

    /**
     * Constructor
     *
     * @param channelInitializer @ChannelInitializer for channel
     */
    protected NettyTransportClientConfig(final NettyChannelInitializer channelInitializer) {
        this.workerGroup = new NioEventLoopGroup(0);
        this.channelInitializer = channelInitializer;
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }


    /**
     * Constructor
     *
     * @param workerGroup        @NioEventLoopGroup for worker
     * @param channelInitializer @ChannelInitializer associated with channel
     */
    public NettyTransportClientConfig(final NioEventLoopGroup workerGroup,
                                      final NettyChannelInitializer channelInitializer) {
        this.workerGroup = workerGroup;
        this.channelInitializer = channelInitializer;
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }


    /**
     * Factory
     *
     * @param workerGroup        @NioEventLoopGroup for worker
     * @param channelInitializer @ChannelInitializer associated with channel
     * @return
     */
    public static NettyTransportClientConfig getDefault(final NioEventLoopGroup workerGroup,
                                                        final NettyChannelInitializer channelInitializer) {
        return new NettyTransportClientConfig(workerGroup, channelInitializer);
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
