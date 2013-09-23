package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportServer;
import com.rc.transporter.core.ITransportSession;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
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
public final class NettyTransportServer implements ITransportServer {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportServer.class);
    /**
     * Server config
     */
    private NettyTransportServerConfig serverConfig;
    /**
     * Constructor
     *
     * @param config netty server config
     */
    public NettyTransportServer(final NettyTransportServerConfig config) {
        this.serverConfig = config;
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
                      final ITransportSession nettyTransportSession) {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.serverConfig.getBossGroup(), this.serverConfig.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        /**
                         * This method will be called once the {@link io.netty.channel.Channel} was registered. After the method returns this instance
                         * will be removed from the {@link io.netty.channel.ChannelPipeline} of the {@link io.netty.channel.Channel}.
                         *
                         * @param ch the {@link io.netty.channel.Channel} which was registered.
                         * @throws Exception is thrown if an error occours. In that case the {@link io.netty.channel.Channel} will be closed.
                         */
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            for (Map.Entry<String, ChannelHandler> pipelineEntry : serverConfig.getPipeline().entrySet()) {
                                pipeline.addLast(pipelineEntry.getKey(), pipelineEntry.getValue());
                            }
                            pipeline.addLast("handler", new NettyTransportSession(nettyTransportSession));
                        }
                    });

            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            // bind server
            serverBootstrap.bind(hostname, port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.serverConfig.getBossGroup().shutdownGracefully();
            this.serverConfig.getWorkerGroup().shutdownGracefully();
        }
    }
}
