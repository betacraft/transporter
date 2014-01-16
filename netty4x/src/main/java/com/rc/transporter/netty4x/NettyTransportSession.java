package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportSession;
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
public class NettyTransportSession<I, O> extends SimpleChannelInboundHandler<Object> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportSession.class);
    /**
     * @NettyChannel associated with this session
     */
    private NettyChannel<O> nettyChannel;
    /**
     * Underlying @ITransportSession
     */
    private ITransportSession<I, O> transportSession;


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.nettyChannel = new NettyChannel<O>(ctx);
        transportSession.onConnected(this.nettyChannel);
    }

    /**
     * Constructor
     *
     * @param transportSession @ITransportSession which receives event
     */
    public NettyTransportSession (ITransportSession<I, O> transportSession) {
        this.transportSession = transportSession;
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
        transportSession.onData((I) msg);
    }

    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void userEventTriggered (ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof ChannelInputShutdownEvent) {
            logger.error("Disconnecting channel because of input shutdown");
            if (transportSession != null)
                transportSession.onDisconnected();
        }
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
        transportSession.onDisconnected();
        logger.info("Channel inactive " + ctx.name());
        if (this.nettyChannel != null)
            this.nettyChannel.close();
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
        transportSession.onError(cause);
        if (this.nettyChannel != null)
            this.nettyChannel.closeChannel();
    }
}
