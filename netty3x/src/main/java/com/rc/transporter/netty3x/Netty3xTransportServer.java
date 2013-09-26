package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;

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
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession transportSession) throws Exception {
        this.bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        serverConfig.getBossExecutors(),
                        serverConfig.getWorkerExecutors()));
        this.bootstrap.setOptions(this.serverConfig.getChannelOptions());
        this.serverConfig.getChannelPipelineFactory().setRuntimeChannelHandlerProvider(new Netty3xChannelPipelineFactory.RuntimeChannelHandlerProvider() {
            @Override
            public ChannelHandler get() {
                return new Netty3xTransportSession(transportSession);
            }
        });
        // Set up the pipeline factory.
        this.bootstrap.setPipelineFactory(this.serverConfig.getChannelPipelineFactory());
        // Bind and start to accept incoming connections.
        this.bootstrap.bind(new InetSocketAddress(port)).getCloseFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                transportServerListener.onClosed();
            }
        });
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
        this.serverConfig.getChannelPipelineFactory().setRuntimeChannelHandlerProvider(new Netty3xChannelPipelineFactory.RuntimeChannelHandlerProvider() {
            @Override
            public ChannelHandler get() {
                return new Netty3xTransportSession(transportSession);
            }
        });
        // Set up the pipeline factory.
        this.bootstrap.setPipelineFactory(this.serverConfig.getChannelPipelineFactory());

        // Bind and start to accept incoming connections.
        this.bootstrap.bind(new InetSocketAddress(port)).getCloseFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                transportServerListener.onClosed();
            }
        });
    }

    /**
     * Method to start server
     *
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSessionFactory @ITransportSessionFactory session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final int port, final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        this.bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        serverConfig.getBossExecutors(),
                        serverConfig.getWorkerExecutors()));
        this.bootstrap.setOptions(this.serverConfig.getChannelOptions());
        this.serverConfig.getChannelPipelineFactory().setRuntimeChannelHandlerProvider(new Netty3xChannelPipelineFactory.RuntimeChannelHandlerProvider() {
            @Override
            public ChannelHandler get() {
                return new Netty3xTransportSession(transportSessionFactory.get());
            }
        });
        // Set up the pipeline factory.
        this.bootstrap.setPipelineFactory(this.serverConfig.getChannelPipelineFactory());
        // Bind and start to accept incoming connections.
        this.bootstrap.bind(new InetSocketAddress(port)).getCloseFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                transportServerListener.onClosed();
            }
        });
    }

    /**
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSessionFactory @ITransportSessionFactory factory for session associated with server connections
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final String hostname, final int port,
                      final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        this.bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        serverConfig.getBossExecutors(),
                        serverConfig.getWorkerExecutors()));
        this.bootstrap.setOptions(this.serverConfig.getChannelOptions());
        this.serverConfig.getChannelPipelineFactory().setRuntimeChannelHandlerProvider(new Netty3xChannelPipelineFactory.RuntimeChannelHandlerProvider() {
            @Override
            public ChannelHandler get() {
                return new Netty3xTransportSession(transportSessionFactory.get());
            }
        });
        // Set up the pipeline factory.
        this.bootstrap.setPipelineFactory(this.serverConfig.getChannelPipelineFactory());
        // Bind and start to accept incoming connections.
        this.bootstrap.bind(new InetSocketAddress(hostname, port)).getCloseFuture().addListener(new ChannelFutureListener() {
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
