package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.rc.transporter.netty4x.DynamicNettyIncomingTransportSession;
import com.rc.transporter.netty4x.IDynamicNettyTransportSessionFactory;
import io.netty.channel.Channel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Extension to socketio channel initializer
 * Author: akshay
 * Date  : 11/8/13
 * Time  : 9:59 PM
 */
final class SocketIOPipelineFactory extends SocketIOChannelInitializer {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIOPipelineFactory.class);
    /**
     * Channel handlers
     */
    private ArrayList<IDynamicNettyTransportSessionFactory> handlers;


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
    public void setHandlers (final ArrayList<IDynamicNettyTransportSessionFactory> handlers) {
        this.handlers = handlers;
    }

    /**
     * Method to add an handler
     *
     * @param handler
     */
    public void addHandler (final IDynamicNettyTransportSessionFactory handler) {
        if (this.handlers == null) {
            this.handlers = new ArrayList<IDynamicNettyTransportSessionFactory>();
        }
        this.handlers.add(handler);
    }


    /**
     * Factory
     *
     * @return instance of @SocketIOChannelInitializers
     */
    static SocketIOPipelineFactory get () {
        logger.trace("creating new pipeline factory");
        return new SocketIOPipelineFactory();
    }


    @Override
    protected void initChannel (Channel ch) throws Exception {
        super.initChannel(ch);
        for (IDynamicNettyTransportSessionFactory transportSessionFactory : this.handlers) {
            switch (transportSessionFactory.addAt()) {
                case ADD_FIRST:
                    ch.pipeline().addFirst(transportSessionFactory.getName(),
                            new DynamicNettyIncomingTransportSession(transportSessionFactory.get()));
                    break;
                case ADD_LAST:
                    ch.pipeline().addLast(transportSessionFactory.getName(),
                            new DynamicNettyIncomingTransportSession(transportSessionFactory.get()));
                    break;
            }
        }
        ch.pipeline().addFirst("logger", new LoggingHandler(LogLevel.TRACE));
    }
}
