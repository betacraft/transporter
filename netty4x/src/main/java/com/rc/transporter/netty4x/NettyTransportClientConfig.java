package com.rc.transporter.netty4x;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty transport client config
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 4:03 AM
 */
public class NettyTransportClientConfig {
    /**
     * Session event executor group factory
     */
    public interface SessionEventExecutorGroupFactory {
        public EventExecutorGroup get ();
    }

    /**
     * Nio event loop group factory
     */
    public interface NioEventLoopGroupFactory {
        public NioEventLoopGroup get ();
    }

    /**
     * Worker group
     */
    private NioEventLoopGroupFactory nioGroupFactory;
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
    private SessionEventExecutorGroupFactory sessionEventsExecutorFactory;
    /**
     * keep nio event loop group alive
     */
    private AtomicBoolean keepNioGroupAlive = new AtomicBoolean(false);
    /**
     * Executor group alive flag
     */
    private AtomicBoolean keepExecutorGroupAlive = new AtomicBoolean(false);
    /**
     * Auto recover on disconnect
     */
    private AtomicBoolean autoRecover = new AtomicBoolean(false);
    /**
     * Auto recover attempts
     */
    private int autoRecoverAttempts = 3;


    /**
     * Getter for @autoRecover
     *
     * @return
     */
    public boolean getAutoRecover () {
        return this.autoRecover.get();
    }

    /**
     * Setter for @autoRecover
     *
     * @param autoRecover
     */
    public void setAutoRecover (final boolean autoRecover) {
        this.autoRecover.set(autoRecover);
    }

    /**
     * getter for @autoRecoverAttempts
     *
     * @return
     */
    public int getAutoRecoverAttempts () {
        return this.autoRecoverAttempts;
    }

    /**
     * setter for @autoRecoverAttempts
     *
     * @param autoRecoverAttempts
     */
    public void setAutoRecoverAttempts (final int autoRecoverAttempts) {
        this.autoRecoverAttempts = autoRecoverAttempts;
    }

    /**
     * Getter for @keepExecutorGroupAlive
     *
     * @return
     */
    public boolean getKeepExecutorGroupAlive () {
        return keepExecutorGroupAlive.get();
    }


    /**
     * Setter for @keepExecutorGroupAlive
     *
     * @param keepExecutorGroupAlive
     */
    public void setKeepExecutorGroupAlive (final boolean keepExecutorGroupAlive) {
        this.keepExecutorGroupAlive.set(keepExecutorGroupAlive);
    }


    /**
     * Getter for @keepNioGroupAlive
     *
     * @return
     */
    public boolean getKeepNioGroupAlive () {
        return keepNioGroupAlive.get();
    }

    /**
     * Setter for @keepNioGroupAlive
     *
     * @param keepNioGroupAlive
     */
    public void setKeepNioGroupAlive (final boolean keepNioGroupAlive) {
        this.keepNioGroupAlive.set(keepNioGroupAlive);
    }

    /**
     * Getter for @sessionEventsExecutorFactory
     *
     * @return
     */
    public SessionEventExecutorGroupFactory getSessionEventsExecutorFactory () {
        return sessionEventsExecutorFactory;
    }

    /**
     * Setter for @sessionEventsExecutorFactory
     *
     * @param sessionEventsExecutorFactory
     */
    public void setSessionEventsExecutorFactory (final SessionEventExecutorGroupFactory
            sessionEventsExecutorFactory) {
        this.sessionEventsExecutorFactory = sessionEventsExecutorFactory;
    }

    /**
     * Getter for @channelInitializer
     *
     * @return
     */
    public NettyChannelInitializer getChannelInitializer () {
        return channelInitializer;
    }

    /**
     * Setter for @channelInitializer
     *
     * @param channelInitializer
     */
    public void setChannelInitializer (NettyChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    /**
     * Getter for @channelOptions
     *
     * @return
     */
    public Map<ChannelOption, Object> getChannelOptions () {
        return channelOptions;
    }

    /**
     * Setter for @channelOptions
     *
     * @param channelOptions
     */
    public void setChannelOptions (Map<ChannelOption, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    /**
     * Method add a channel option
     *
     * @param channelOption @ChannelOption
     * @param value         value
     */
    public void addChannelOption (ChannelOption channelOption, Object value) {
        if (this.channelOptions == null)
            this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(channelOption, value);
    }

    /**
     * Method to remove a channel option
     *
     * @param channelOption @ChannelOption
     */
    public void removeChannelOption (ChannelOption channelOption) {
        if (this.channelOptions == null)
            return;
        this.channelOptions.remove(channelOption);
    }

    /**
     * Getter for @nioGroupFactory
     *
     * @return
     */
    public NioEventLoopGroupFactory getWorkerGroupFactory () {
        return nioGroupFactory;
    }

    /**
     * Setter for @nioGroupFactory
     *
     * @param workerGroup
     */
    public void setWorkerGroupFactory (NioEventLoopGroupFactory workerGroup) {
        this.nioGroupFactory = workerGroup;
    }


    /**
     * Default constructor
     */
    protected NettyTransportClientConfig () {

        this.channelOptions = new HashMap<ChannelOption, Object>();
    }

    /**
     * Constructor
     *
     * @param channelInitializer @ChannelInitializer for channel
     */
    protected NettyTransportClientConfig (final NettyChannelInitializer channelInitializer) {
        this.nioGroupFactory = new NioEventLoopGroupFactory() {
            @Override
            public NioEventLoopGroup get () {
                return new NioEventLoopGroup(0);
            }
        };
        this.channelInitializer = channelInitializer;
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.SO_SNDBUF, 1048576);
        this.channelOptions.put(ChannelOption.SO_RCVBUF, 1048576);
        this.channelOptions.put(ChannelOption.ALLOW_HALF_CLOSURE, true);
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }


    /**
     * Constructor
     *
     * @param nioGroupFactory    @NioEventLoopGroupFactory for worker
     * @param channelInitializer @ChannelInitializer associated with channel
     */
    protected NettyTransportClientConfig (final NioEventLoopGroupFactory nioGroupFactory,
            final NettyChannelInitializer channelInitializer) {
        this.nioGroupFactory = nioGroupFactory;
        this.channelInitializer = channelInitializer;
        // initializing default channel options
        this.channelOptions = new HashMap<ChannelOption, Object>();
        this.channelOptions.put(ChannelOption.SO_SNDBUF, 1048576);
        this.channelOptions.put(ChannelOption.SO_RCVBUF, 1048576);
        this.channelOptions.put(ChannelOption.ALLOW_HALF_CLOSURE, true);
        this.channelOptions.put(ChannelOption.TCP_NODELAY, true);
        this.channelOptions.put(ChannelOption.SO_KEEPALIVE, true);
    }


    /**
     * Factory
     *
     * @param nioGroupFactory    @NioEventLoopGroupFactory for worker
     * @param channelInitializer @ChannelInitializer associated with channel
     * @return
     */
    public static NettyTransportClientConfig getDefault (final NioEventLoopGroupFactory nioGroupFactory,
            final NettyChannelInitializer channelInitializer) {
        return new NettyTransportClientConfig(nioGroupFactory, channelInitializer);
    }


    /**
     * Factory for clean slate
     *
     * @return
     */
    public static NettyTransportClientConfig get () {
        return new NettyTransportClientConfig();
    }
}
