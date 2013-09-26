package com.rc.transporter.netty3x;

import java.util.concurrent.Executors;

/**
 * Netty 3x transport server config
 * Author: akshay
 * Date  : 9/25/13
 * Time  : 12:25 AM
 */
public class Netty3xTransportServerConfig extends Netty3xTransportClientConfig {
    /**
     * Constructor
     */
    private Netty3xTransportServerConfig() {
        super();
    }


    /**
     * Getter to get default client configuration
     *
     * @param channelPipelineFactory @Netty3xChannelPipelineFactory for the connection
     * @return @Netty3xTransportClientConfig instance with some pre-configuration
     */
    public static Netty3xTransportServerConfig getDefault(final Netty3xChannelPipelineFactory channelPipelineFactory) {
        Netty3xTransportServerConfig serverConfig = new Netty3xTransportServerConfig();
        serverConfig.bossExecutors = Executors.newCachedThreadPool();
        serverConfig.workerExecutors = Executors.newCachedThreadPool();
        serverConfig.channelPipelineFactory = channelPipelineFactory;
        serverConfig.channelOptions.put("tcpNoDelay", true);
        serverConfig.channelOptions.put("soKeepAlive", true);
        return serverConfig;
    }

    /**
     * Default factory
     *
     * @return clean slate instance of @Netty3xTransportClientConfig
     */
    public static Netty3xTransportServerConfig get() {
        return new Netty3xTransportServerConfig();
    }

}
