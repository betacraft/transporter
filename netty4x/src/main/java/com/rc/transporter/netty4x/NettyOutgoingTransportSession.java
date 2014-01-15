package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportOutgoingSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty transport system
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:44 AM
 */
public class NettyOutgoingTransportSession<I, O> extends SimpleChannelInboundHandler<Object> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyOutgoingTransportSession.class);
    /**
     * @NettyChannel associated with this session
     */
    protected NettyChannel<O> nettyChannel;
    /**
     * Underlying @ITransportIncomingSession
     */
    protected ITransportOutgoingSession<I, O> transportSession;
    /**
     * Is closed
     */
    protected AtomicBoolean isClosed = new AtomicBoolean(false);
    /**
     * Session state listener
     */
    protected NettyOutgoingChannelStateListener sessionStateListener;
    /**
     * is recovered flag
     */
    private AtomicBoolean isRecovered = new AtomicBoolean(false);


    /**
     * Constructor
     *
     * @param transportSession @ITransportIncomingSession which receives event
     */
    public NettyOutgoingTransportSession (final ITransportOutgoingSession<I, O> transportSession,
            final NettyOutgoingChannelStateListener outgoingChannelStateListener,
            final boolean isRecovered) {
        this.transportSession = transportSession;
        this.sessionStateListener = outgoingChannelStateListener;
        this.isRecovered.set(isRecovered);
    }


    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.nettyChannel = new NettyChannel<O>(ctx);
        if (this.isRecovered.get()) {
            transportSession.onRecovered(this.nettyChannel);
        } else {
            transportSession.onConnected(this.nettyChannel);
        }
    }

    /**
     * @param ctx the {@link io.netty.channel.ChannelHandlerContext} which this {@link io.netty.channel
     *            .SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.transportSession != null)
            this.transportSession.onData((I) msg);
    }

    @Override
    public void userEventTriggered (ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.debug("User event " + evt.toString());
        if (evt instanceof ChannelInputShutdownEvent) {
            //TODO check if this is correct
            // for now triggering close socket event
            ctx.close();
        }
        super.userEventTriggered(ctx, evt);

    }


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelInactive (ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel inactive");
        super.channelInactive(ctx);
        if (this.isClosed.get()) {
            if (this.transportSession != null)
                transportSession.onDisconnected();
        } else {
            logger.debug("session got dropped");
            this.sessionStateListener.onSessionDropped();
        }
    }

    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link io.netty.channel.ChannelHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Exception caught ", cause);
        if (this.isClosed.get()) {
            if (this.transportSession != null)
                transportSession.onError(cause);
            if (this.nettyChannel != null)
                this.nettyChannel.close();
        } else {
            this.sessionStateListener.onSessionDropped(cause);
        }
    }


    /**
     * Close session
     */
    public void closeSession () {
        this.isClosed.set(true);
        logger.debug("Closing session");
        if (this.nettyChannel != null)
            this.nettyChannel.close();
    }
}
