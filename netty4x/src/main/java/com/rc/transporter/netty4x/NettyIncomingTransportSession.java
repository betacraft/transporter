package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportIncomingSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty transport system
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:44 AM
 */
public class NettyIncomingTransportSession<I, O> extends SimpleChannelInboundHandler<Object> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyIncomingTransportSession.class);
    /**
     * @NettyChannel associated with this session
     */
    protected NettyChannel<O> nettyChannel;
    /**
     * Underlying @ITransportIncomingSession
     */
    protected ITransportIncomingSession<I, O> transportSession;


    /**
     * Constructor
     *
     * @param transportSession @ITransportIncomingSession which receives event
     */
    public NettyIncomingTransportSession (final ITransportIncomingSession<I, O> transportSession) {
        this.transportSession = transportSession;
    }

    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.nettyChannel = new NettyChannel<O>(ctx);
        if (transportSession != null)
            transportSession.onConnected(this.nettyChannel);
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
        logger.debug("got data");
        if (transportSession != null)
            transportSession.onData((I) msg);
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
        super.channelInactive(ctx);
        if (transportSession != null)
            transportSession.onDisconnected();
        logger.info("Channel inactive " + ctx.name());
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
        if (transportSession != null) {
            transportSession.onError(cause);
        }
        if (this.nettyChannel != null)
            this.nettyChannel.close();
    }

}
