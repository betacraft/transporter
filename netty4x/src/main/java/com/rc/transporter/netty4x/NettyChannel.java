package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportChannel;
import io.netty.channel.ChannelHandlerContext;

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
    public enum NettyChannelProperty {
        CLOSE_READ,
        CLOSE_WRITE,


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

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    @Override
    public void setProperty(String name, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
