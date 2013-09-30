package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Netty transport server
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 3:54 AM
 */
public final class NettyTransportServer extends TransportServer {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportServer.class);
    /**
     * Server config
     */
    private NettyTransportServerConfig serverConfig;
    /**
     * Boss group
     */
    private NioEventLoopGroup bossGroup;
    /**
     * Worker group
     */
    private NioEventLoopGroup workerGroup;


    /**
     * Constructor
     *
     * @param config netty server config
     */
    public NettyTransportServer(final NettyTransportServerConfig config) {
        super();
        this.serverConfig = config;
    }

    /**
     * Method to start server
     *
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param nettyTransportSession   @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(int port, final ITransportServerListener transportServerListener,
                      final ITransportSession nettyTransportSession)
            throws Exception {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            this.serverConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {

                @Override
                public void appendRuntimeHandler(final ChannelPipeline pipeline) {
                    if (serverConfig.getSessionEventsExecutorFactory() == null) {
                        logger.warn("No session events executor factory assigned\nMake sure you are not stealing time from worker group for better performance");
                        pipeline.addLast(new NettyTransportSession(nettyTransportSession));
                    } else {
                        pipeline.addLast(serverConfig.getSessionEventsExecutorFactory().get(), new NettyTransportSession(nettyTransportSession));
                    }
                }
            });
            this.bossGroup = this.serverConfig.getBossGroupFactory().get();
            this.workerGroup = this.serverConfig.getWorkerGroupFactory().get();

            serverBootstrap.group(this.bossGroup, this.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverConfig.getChannelInitializer());
            // bind server
            serverBootstrap.bind(port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            throw exception;
        } finally {
            close();
        }
    }

    /**
     * Starting server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param nettyTransportSession   @ITransportSession associated with each connection being received on this server
     */
    @Override
    public void start(final String hostname, final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession nettyTransportSession) throws Exception {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            this.serverConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {

                @Override
                public void appendRuntimeHandler(final ChannelPipeline pipeline) {
                    if (serverConfig.getSessionEventsExecutorFactory() == null)
                        pipeline.addLast(new NettyTransportSession(nettyTransportSession));
                    else
                        pipeline.addLast(serverConfig.getSessionEventsExecutorFactory().get(), new NettyTransportSession(nettyTransportSession));
                }
            });
            this.bossGroup = this.serverConfig.getBossGroupFactory().get();
            this.workerGroup = this.serverConfig.getWorkerGroupFactory().get();
            serverBootstrap.group(this.bossGroup, this.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverConfig.getChannelInitializer());
            // bind server
            serverBootstrap.bind(hostname, port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            throw exception;
        } finally {
            close();
        }
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
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            this.serverConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {

                @Override
                public void appendRuntimeHandler(final ChannelPipeline pipeline) {
                    if (serverConfig.getSessionEventsExecutorFactory() == null)
                        pipeline.addLast(new NettyTransportSession(transportSessionFactory.get()));
                    else
                        pipeline.addLast(serverConfig.getSessionEventsExecutorFactory().get(), new NettyTransportSession(transportSessionFactory.get()));
                }
            });
            this.bossGroup = this.serverConfig.getBossGroupFactory().get();
            this.workerGroup = this.serverConfig.getWorkerGroupFactory().get();
            serverBootstrap.group(this.bossGroup, this.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverConfig.getChannelInitializer());
            // bind server
            serverBootstrap.bind(port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            throw exception;
        } finally {
            close();
        }
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
    public void start(final String hostname, final int port, final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            this.serverConfig.getChannelInitializer().setRuntimeHandlerProvider(new NettyChannelInitializer.RuntimeHandlerProvider() {
                @Override
                public void appendRuntimeHandler(ChannelPipeline pipeline) {
                    if (serverConfig.getSessionEventsExecutorFactory() == null)
                        pipeline.addLast(new NettyTransportSession(transportSessionFactory.get()));
                    else
                        pipeline.addLast(serverConfig.getSessionEventsExecutorFactory().get(), new NettyTransportSession(transportSessionFactory.get()));
                }
            });
            this.bossGroup = this.serverConfig.getBossGroupFactory().get();
            this.workerGroup = this.serverConfig.getWorkerGroupFactory().get();
            serverBootstrap.group(this.bossGroup, this.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverConfig.getChannelInitializer());
            // bind server
            serverBootstrap.bind(hostname, port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            throw exception;
        } finally {
            close();
        }
    }

    /**
     * Method to close server
     */
    @Override
    protected void close() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}
