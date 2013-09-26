package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.TransportSession;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Netty 3x transport client
 * Author: akshay
 * Date  : 9/24/13
 * Time  : 2:06 AM
 */
public final class Netty3xTransportClient<I, O> implements ITransportClient<I, O> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Netty3xTransportClient.class);
    /**
     * Client config
     */
    private Netty3xTransportClientConfig clientConfig;
    /**
     * Client bootstrap
     */
    private ClientBootstrap clientBootstrap;

    /**
     * Constructor
     *
     * @param clientConfig @Netty3xTransportClientConfig for this connection
     */
    public Netty3xTransportClient(final Netty3xTransportClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }


    /**
     * Method to initialize connection with @TransportServer or other server
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     * @throws @Exception
     */
    @Override
    public void connect(final String host, final int port, final TransportSession<I, O> transportSession) throws Exception {
        try {
            this.clientBootstrap = new ClientBootstrap(
                    new NioClientSocketChannelFactory(
                            this.clientConfig.getBossExecutors(),
                            this.clientConfig.getWorkerExecutors()));
            this.clientBootstrap.setOptions(clientConfig.getChannelOptions());
            // Set up the pipeline factory.
            this.clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline pipeline = Channels.pipeline();
                    for (Map.Entry<String, ChannelHandler> handler : clientConfig.getSharableChannelHandlers().entrySet())
                        pipeline.addLast(handler.getKey(), handler.getValue());
                    pipeline.addLast("handler", new Netty3xTransportSession<I, O>(transportSession));
                    return pipeline;
                }
            });
            // Start the connection attempt.
            this.clientBootstrap.connect(new InetSocketAddress(host, port));
        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Method to close client
     */
    @Override
    public void close() {
        this.clientConfig.getBossExecutors().shutdownNow();
        this.clientConfig.getWorkerExecutors().shutdownNow();
        this.clientBootstrap.shutdown();
    }
}
