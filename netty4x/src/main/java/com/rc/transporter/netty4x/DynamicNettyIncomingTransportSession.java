package com.rc.transporter.netty4x;

import io.netty.channel.*;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty transport system
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:44 AM
 */
public class DynamicNettyIncomingTransportSession<I, O> extends SimpleChannelInboundHandler<Object> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DynamicNettyIncomingTransportSession.class);
    /**
     * is removed
     */
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    /**
     * validated
     */
    private AtomicBoolean isValidated = new AtomicBoolean(false);

    /**
     * @NettyChannel associated with this session
     */
    private NettyChannel<O> nettyChannel;
    /**
     * Underlying @ITransportIncomingSession
     */
    private IDynamicTransportIncomingSession<I, O> transportSession;
    /**
     * Detect user event for disconnection
     */
    private AtomicBoolean detectUserEventsForDisconnection = new AtomicBoolean(false);

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
        logger.trace("Got channel connection");
    }

    /**
     * Constructor
     *
     * @param transportSession @ITransportIncomingSession which receives event
     */
    public DynamicNettyIncomingTransportSession (IDynamicTransportIncomingSession<I, O> transportSession,
            final boolean detectUserEventForDisconnection) {
        this.transportSession = transportSession;
        this.detectUserEventsForDisconnection.set(detectUserEventForDisconnection);
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
        if (!isValidated.get()) {
            logger.trace("Got data " + msg.toString());
            if (transportSession.validate((I) msg)) {
                logger.trace("session validated");
                isValidated.set(true);
                // removing all handlers if the underlying session is standalone
                if (transportSession.isStandalone()) {
                    for (Map.Entry<String, ChannelHandler> handler : ctx.pipeline().toMap().entrySet()) {
                        if (handler.getValue().equals(this)) {
                            logger.trace("keeping " + handler.getKey());
                            continue;
                        }
                        if (handler.getValue() instanceof ChannelOutboundHandler
                                || handler.getValue() instanceof ChannelOutboundHandlerAdapter
                                || handler.getKey().equals("logger"))
                            continue;
                        logger.trace("Removing " + handler.getKey());
                        ctx.pipeline().remove(handler.getKey());
                    }

                }
            } else {
                isClosed.set(true);
                logger.trace("Removing dynamic transport session");
                ctx.pipeline().remove(((IDynamicTransportIncomingSession) transportSession).getName());
                ctx.fireChannelRead(msg);
            }
            return;
        }
        if (!isClosed.get() && transportSession != null)
            transportSession.onData((I) msg);
    }


    @Override
    public void userEventTriggered (ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        logger.debug("User event " + evt.toString());
        if (isClosed.get() || !isValidated.get())
            return;
        if (this.detectUserEventsForDisconnection.get()) {
            if (evt instanceof ChannelInputShutdownEvent) {
                //TODO check if this is correct
                // for now triggering close socket event
                logger.trace("Closing session because of channel input shutdown event");
                ctx.close();
            }
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
        if (isClosed.get() || !isValidated.get())
            return;
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
        if (isClosed.get() || !isValidated.get())
            return;
        transportSession.onError(cause);
        this.nettyChannel.close();
    }
}
