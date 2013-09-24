package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Author: akshay
 * Date  : 9/25/13
 * Time  : 12:30 AM
 */
public class Netty3xTransportServer extends TransportServer {

    /**
     * @Netty3xTransportServerConfig
     */
    private Netty3xTransportServerConfig serverConfig;
    /**
     * Server bootstrap
     */
    private ServerBootstrap bootstrap;

    /**
     * Constructor
     *
     * @param serverConfig @Netty3xTransportServerConfig server config
     */
    public Netty3xTransportServer(Netty3xTransportServerConfig serverConfig) {
        super();
        this.serverConfig = serverConfig;
    }

    /**
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final String hostname, final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession transportSession) throws Exception {
        this.bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        serverConfig.getBossExecutors(),
                        serverConfig.getWorkerExecutors()));
        this.bootstrap.setOptions(this.serverConfig.getChannelOptions());
        // Set up the pipeline factory.
        this.bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                for (Map.Entry<String, ChannelHandler> handler : serverConfig.getChannelHandlers().entrySet())
                    pipeline.addLast(handler.getKey(), handler.getValue());
                pipeline.addLast("handler", new Netty3xTransportSession(transportSession));
                return pipeline;
            }
        });

        // Bind and start to accept incoming connections.
        this.bootstrap.bind(new InetSocketAddress(port)).getCloseFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                transportServerListener.onClosed();
            }
        });
    }

    /**
     * Method to close server
     */
    @Override
    protected void close() {
        this.bootstrap.shutdown();

    }
}
