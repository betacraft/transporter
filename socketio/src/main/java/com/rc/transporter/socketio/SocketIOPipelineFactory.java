package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.netty4x.NettyTransportSession;
import io.netty.channel.Channel;

import java.util.ArrayList;

/**
 * Extension to socketio channel initializer
 * Author: akshay
 * Date  : 11/8/13
 * Time  : 9:59 PM
 */
final class SocketIOPipelineFactory extends SocketIOChannelInitializer {
    /**
     * Channel handlers
     */
    private ArrayList<ITransportSession> handlers;


    /**
     * Constructors
     */
    private SocketIOPipelineFactory () {
        // do nothing
    }

    /**
     * Setter for @handlers
     *
     * @param handlers
     */
    public void setHandlers (final ArrayList<ITransportSession> handlers) {
        this.handlers = handlers;
    }

    /**
     * Method to add an handler
     *
     * @param handler
     */
    public void addHandler (final ITransportSession handler) {
        if (this.handlers == null) {
            this.handlers = new ArrayList<ITransportSession>();
        }
        this.handlers.add(handler);
    }


    /**
     * Factory
     *
     * @return instance of @SocketIOChannelInitializers
     */
    static SocketIOPipelineFactory get () {
        return new SocketIOPipelineFactory();
    }


    @Override
    protected void initChannel (Channel ch) throws Exception {
        super.initChannel(ch);
        for (ITransportSession transportSession : this.handlers) {
            ch.pipeline().addLast(new NettyTransportSession(transportSession));
        }
    }
}
