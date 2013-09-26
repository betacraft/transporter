package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:57 AM
 */
public final class NettyChannel<M> extends TransportChannel<M> {
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
    public NettyChannel(ChannelHandlerContext nettyChannelHandlerContext) {
        this.nettyChannelHandlerContext = nettyChannelHandlerContext;
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData(M data) {
        //logger.debug("Sending data over netty channel " + data);
        if (data instanceof ByteBuf) {
            this.nettyChannelHandlerContext.writeAndFlush(((ByteBuf) data).retain());
        }
        this.nettyChannelHandlerContext.writeAndFlush(data);
    }

    /**
     * Method to close this channel
     */
    @Override
    protected void closeChannel() {
        if (this.channelClosed.get())
            return;
        this.channelClosed.set(true);
        this.nettyChannelHandlerContext.disconnect();
        this.nettyChannelHandlerContext.close();
    }


}
