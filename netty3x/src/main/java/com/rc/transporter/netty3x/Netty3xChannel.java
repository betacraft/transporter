package com.rc.transporter.netty3x;

import com.rc.transporter.core.TransportChannel;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/24/13
 * Time  : 1:56 AM
 */
public final class Netty3xChannel<M> extends TransportChannel<M> {
    /**
     * Channel associated with this transport
     */
    private Channel channel;
    /**
     * Flag to check if channel is closed
     */
    private AtomicBoolean isChannelClosed = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param channelHandlerContext @ChannelHandlerContext associated with this channel
     */
    public Netty3xChannel(final ChannelHandlerContext channelHandlerContext) {
        this.channel = channelHandlerContext.getChannel();
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData(M data) {
        this.channel.write(data);

    }

    /**
     * Method to close channel
     */
    @Override
    protected void closeChannel() {
        if (this.isChannelClosed.get())
            return;
        this.isChannelClosed.set(true);
        this.channel.disconnect();
        this.channel.close();
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
