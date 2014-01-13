package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportCallback;
import com.rc.transporter.core.ITransportChannel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:57 AM
 */
public final class NettyChannel<M> implements ITransportChannel<M> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyChannel.class);

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
    public void sendData (final M data, final ITransportCallback callback) {
        if (isOpen())
            this.nettyChannelHandlerContext.writeAndFlush(data);
    }

    /**
     * Method to close this channel
     */
    @Override
    public void close () {
        logger.debug("Close channel request");
        if (this.channelClosed.getAndSet(true))
            return;
        logger.debug("Closing channel");
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

}
