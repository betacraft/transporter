package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty 3x based decoder
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 3:10 AM
 */
public final class Netty3xDecoder<I> extends FrameDecoder {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Netty3xDecoder.class);
    /**
     * Underlying decoder
     */
    private ITransportDecoder<I> decoder;

    public Netty3xDecoder(ITransportDecoder<I> decoder) {
        this.decoder = decoder;
    }

    /**
     * Decodes the received packets so far into a frame.
     * <p/>
     * If an sub-class wants to extract a frame out of the buffer it should use
     * the {@link #extractFrame(org.jboss.netty.buffer.ChannelBuffer, int, int)} method,
     * to make optimizations easier later.
     *
     * @param ctx     the context of this handler
     * @param channel the current channel
     * @param buffer  the cumulative buffer of received packets so far.
     *                Note that the buffer might be empty, which means you
     *                should not make an assumption that the buffer contains
     *                at least one byte in your decoder implementation.
     * @return the decoded frame if a full frame was received and decoded.
     *         {@code null} if there's not enough data in the buffer to decode a frame.
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        return this.decoder.decode((I) buffer);
    }
}
