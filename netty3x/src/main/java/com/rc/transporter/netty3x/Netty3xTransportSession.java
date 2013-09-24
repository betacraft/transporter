package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import org.jboss.netty.channel.*;

/**
 * Author: akshay
 * Date  : 9/24/13
 * Time  : 1:44 AM
 */
public final class Netty3xTransportSession<M> extends SimpleChannelUpstreamHandler {
    /**
     * @Netty3xChannel associated with this session
     */
    private Netty3xChannel channel;
    /**
     * Underlying @ITransportSession
     */
    private ITransportSession<M> transportSession;


    /**
     * Constructor
     *
     * @param transportSession @ITransportSession that receives events related to this session
     */
    public Netty3xTransportSession(ITransportSession<M> transportSession) {
        this.transportSession = transportSession;
    }

    /**
     * Called when channel is opened
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelOpen(ctx, e);
        this.channel = new Netty3xChannel(ctx);
        this.transportSession.onConnected(this.channel);
    }

    /**
     * Called when channel is closed
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.transportSession.onDisconnected();
        super.channelClosed(ctx, e);

    }

    /**
     * Called when message is received
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        this.transportSession.onData((M) e.getMessage());
    }

    /**
     * Called when exception is caught
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        this.channel.closeChannel();
        this.transportSession.onError(e.getCause());
    }
}
