package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:57 AM
 */
public final class NettyChannel<M> extends TransportChannel<M> {
    /**
     * Enum associated with property
     */
    public static class NettyChannelProperty {
        public static final String AUTO_READ = "AUTO_READ";
    }

    /**
     * @ChannelHandlerContext for this channel
     */
    private ChannelHandlerContext nettyChannelHandlerContext;

    /**
     * Flag to check if channel closed
     */
    private AtomicBoolean channelClosed = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param nettyChannelHandlerContext @ChannelHandlerContext associated with this instance
     */
    public NettyChannel (ChannelHandlerContext nettyChannelHandlerContext) {
        this.nettyChannelHandlerContext = nettyChannelHandlerContext;
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData (M data) {
        if (isOpen())
            this.nettyChannelHandlerContext.writeAndFlush(data);
    }

    @Override
    public void sendData (final M data, final IDataSendListener dataSendListener) {
        if (isOpen()) {
            this.nettyChannelHandlerContext.writeAndFlush(data).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete (ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        dataSendListener.sendComplete(data);
                    } else {
                        dataSendListener.sendFailure(data, future.cause());
                    }
                }
            });
        }
    }

    @Override
    public void sendAndClose (M data) {
        this.nettyChannelHandlerContext.write(data).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Method to close this channel
     */
    @Override
    protected void closeChannel () {
        logger.trace("Close channel request");
        if (this.channelClosed.getAndSet(true))
            return;
        logger.trace("Closing channel");
        this.nettyChannelHandlerContext.disconnect();
        this.nettyChannelHandlerContext.close();
    }

    /**
     * Method to check if channel is open
     */
    @Override
    public boolean isOpen () {
        return !this.channelClosed.get();
    }

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    @Override
    public void setProperty (String name, Object value) {
        // property to set auto-read enabled or disabled
        if (name.equals(NettyChannelProperty.AUTO_READ)) {
            logger.debug("Setting autoread to " + value);
            this.nettyChannelHandlerContext.channel().config().setAutoRead(((Boolean) value));
        }
    }

    public void sendDataWithPromise (M data, ChannelFutureListener channelFutureListener) {
        if (isOpen())
            this.nettyChannelHandlerContext.writeAndFlush(data).addListeners(channelFutureListener);
    }


    public ChannelPipeline getPipeline(){
        return this.nettyChannelHandlerContext.pipeline();
    }

    ChannelHandlerContext getNettyChannelHandlerContext () {
        return this.nettyChannelHandlerContext;
    }

}
