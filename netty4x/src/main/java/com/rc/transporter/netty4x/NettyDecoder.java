package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Message to message netty decoder
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 3:01 AM
 */
public final class NettyDecoder<I> extends MessageToMessageDecoder<Object> {

    /**
     * Logger for logging
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyDecoder.class);

    /**
     * Underlying decoder implementation
     */
    private ITransportDecoder<I> transportDecoder;


    /**
     * Create a new instance which will try to detect the types to match out of the type parameter of the class.
     */
    public NettyDecoder(ITransportDecoder<I> transportDecoder) {
        this.transportDecoder = transportDecoder;
    }

    /**
     * Decode from one message to an other. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link io.netty.channel.ChannelHandlerContext} which this {@link io.netty.handler.codec.MessageToMessageDecoder} belongs to
     * @param msg the message to decode to an other one
     * @param out the {@link java.util.List} to which decoded messages should be added
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        out.add(this.transportDecoder.decode((I) msg));
    }
}
