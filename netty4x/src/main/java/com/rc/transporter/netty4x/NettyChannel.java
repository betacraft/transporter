package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportChannel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:57 AM
 */
public final class NettyChannel extends TransportChannel {

    private ChannelHandlerContext nettyChannelHandlerContext;

    private AtomicBoolean channelClosed = new AtomicBoolean(false);

    public NettyChannel(ChannelHandlerContext nettyChannelHandlerContext) {
        this.nettyChannelHandlerContext = nettyChannelHandlerContext;
    }


    @Override
    public void sendData(Object data) {
        if (this.nettyChannelHandlerContext.channel().isOpen() && !this.channelClosed.get())
            this.nettyChannelHandlerContext.writeAndFlush(data);
    }

    @Override
    public void close() {
        super.close();
        if (this.channelClosed.get())
            return;
        this.channelClosed.set(true);
        this.nettyChannelHandlerContext.disconnect();
        this.nettyChannelHandlerContext.close();
    }
}
