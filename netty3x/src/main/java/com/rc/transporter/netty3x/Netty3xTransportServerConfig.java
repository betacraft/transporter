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
     * @return @Netty3xTransportClientConfig instance with some pre-configuration
     */
    public static Netty3xTransportServerConfig getDefault() {
        Netty3xTransportServerConfig clientConfig = new Netty3xTransportServerConfig();
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
    public static Netty3xTransportServerConfig get() {
        return new Netty3xTransportServerConfig();
    }

}
