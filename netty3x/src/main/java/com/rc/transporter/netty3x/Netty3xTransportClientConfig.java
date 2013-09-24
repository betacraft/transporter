package com.rc.transporter.netty3x;

import org.jboss.netty.channel.ChannelHandler;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty3x client config
 * Author: akshay
 * Date  : 9/24/13
 * Time  : 10:47 PM
 */
public class Netty3xTransportClientConfig {
    /**
     * Boss executors
     */
    protected ExecutorService bossExecutors;
    /**
     * Worker executors
     */
    protected ExecutorService workerExecutors;
    /**
     * Channel handlers
     */
    protected HashMap<String, ChannelHandler> channelHandlers;
    /**
     * Channel options
     */
    protected HashMap<String, Object> channelOptions;


    /**
     * Getter for @bossExecutors
     *
     * @return
     */
    public ExecutorService getBossExecutors() {
        return bossExecutors;
    }

    /**
     * Setter for @bossExecutors
     *
     * @param bossExecutors
     */
    public void setBossExecutors(final ExecutorService bossExecutors) {
        this.bossExecutors = bossExecutors;
    }

    /**
     * Getter for @channelHandlers
     *
     * @return
     */
    public HashMap<String, ChannelHandler> getChannelHandlers() {
        return channelHandlers;
    }

    /**
     * Method to add a channel handler
     *
     * @param name           name of the handler
     * @param channelHandler @ChannelHandler to be added
     * @return
     */
    public Netty3xTransportClientConfig addAnChannelHandler(final String name, final ChannelHandler channelHandler) {
        this.channelHandlers.put(name, channelHandler);
        return this;
    }

    /**
     * Setter for @channelHandlers
     *
     * @param channelHandlers
     */
    public void setChannelHandlers(final HashMap<String, ChannelHandler> channelHandlers) {
        this.channelHandlers = channelHandlers;
    }

    /**
     * Getter for @channelOptions
     *
     * @return
     */
    public HashMap<String, Object> getChannelOptions() {
        return channelOptions;
    }

    /**
     * Setter for @channelOptions
     *
     * @param channelOptions
     */
    public void setChannelOptions(final HashMap<String, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    /**
     * Method to add an option
     *
     * @param name  name of the option
     * @param value value of the option
     */
    public Netty3xTransportClientConfig addChannelOption(final String name, final Object value) {
        this.channelOptions.put(name, value);
        return this;
    }

    /**
     * Getter for @workerExecutors
     *
     * @return
     */
    public ExecutorService getWorkerExecutors() {
        return workerExecutors;
    }

    /**
     * Setter for @workerExecutors
     *
     * @param workerExecutors
     */
    public void setWorkerExecutors(ExecutorService workerExecutors) {
        this.workerExecutors = workerExecutors;
    }

    /**
     * Constructor
     */
    protected Netty3xTransportClientConfig() {
        this.channelHandlers = new HashMap<String, ChannelHandler>();
        this.channelOptions = new HashMap<String, Object>();
    }


    /**
     * Getter to get default client configuration
     *
     * @return @Netty3xTransportClientConfig instance with some pre-configuration
     */
    public static Netty3xTransportClientConfig getDefault() {
        Netty3xTransportClientConfig clientConfig = new Netty3xTransportClientConfig();
        clientConfig.bossExecutors = Executors.newCachedThreadPool();
        clientConfig.workerExecutors = Executors.newCachedThreadPool();
        clientConfig.channelOptions.put("tcpNoDelay", true);
        clientConfig.channelOptions.put("soKeepAlive", true);
        return clientConfig;
    }

    /**
     * Default factory
     *
     * @return clean slate instance of @Netty3xTransportClientConfig
     */
    public static Netty3xTransportClientConfig get() {
        return new Netty3xTransportClientConfig();
    }


}
