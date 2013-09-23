package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import org.jboss.netty.channel.*;

/**
 * Author: akshay
 * Date  : 9/24/13
 * Time  : 1:44 AM
 */
public final class Netty3xTransportSession<M> extends SimpleChannelUpstreamHandler {

    private Netty3xChannel channel;

    private ITransportSession<M> transportSession;


    public Netty3xTransportSession(ITransportSession<M> transportSession) {
        this.transportSession = transportSession;
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelOpen(ctx, e);
        this.channel = new Netty3xChannel(ctx);
        this.transportSession.onConnected(this.channel);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.transportSession.onDisconnected();
        super.channelClosed(ctx, e);

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        this.transportSession.onData((M) e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        this.channel.closeChannel();
        this.transportSession.onError(e.getCause());
    }
}
