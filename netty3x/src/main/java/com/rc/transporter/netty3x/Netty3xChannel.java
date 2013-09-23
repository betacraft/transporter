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
public final class Netty3xChannel extends TransportChannel {

    private Channel channel;


    private AtomicBoolean isChannelClosed = new AtomicBoolean(false);

    public Netty3xChannel(final ChannelHandlerContext channelHandlerContext) {
        this.channel = channelHandlerContext.getChannel();
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData(Object data) {
        this.channel.write(data);

    }

    @Override
    protected void closeChannel() {
        if (this.isChannelClosed.get())
            return;
        this.isChannelClosed.set(true);
        this.channel.disconnect();
        this.channel.close();
    }
}
