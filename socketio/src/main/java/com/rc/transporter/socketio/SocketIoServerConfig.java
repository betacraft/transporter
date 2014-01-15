package com.rc.transporter.socketio;

import com.corundumstudio.socketio.Configuration;
import com.rc.transporter.netty4x.DynamicTransportSessionAddPosition;
import com.rc.transporter.netty4x.IDynamicNettyTransportSessionFactory;
import com.rc.transporter.netty4x.IDynamicTransportSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Socketio server configuration
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 6:59 PM
 */
public final class SocketIoServerConfig {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIoServerConfig.class);

    /**
     * Namespaces associated with server
     */
    private ArrayList<SocketIoServerNamespace> namespaces;
    /**
     * Custom request handler
     */
    private ArrayList<IDynamicNettyTransportSessionFactory> customRequestHandlers;
    /**
     * Socketio related configuration
     */
    private Configuration configuration;

    /**
     * Initialization block
     */ {
        this.namespaces = new ArrayList<SocketIoServerNamespace>();
        this.configuration = new Configuration();
    }


    /**
     * Getter for @configuration
     *
     * @return
     */
    public Configuration getConfiguration () {
        return configuration;
    }


    /**
     * Getter for @namespaces
     *
     * @return
     */
    public ArrayList<SocketIoServerNamespace> getNamespaces () {
        return namespaces;
    }


    /**
     * Add custom request handler
     *
     * @param customRequestHandler
     */
    public void addSharableCustomRequestHandler (final IDynamicTransportSession customRequestHandler) {
        // lazy initialization is used so as to avoid an extra flag
        if (this.customRequestHandlers == null) {
            this.customRequestHandlers = new ArrayList<IDynamicNettyTransportSessionFactory>();
        }
        this.customRequestHandlers.add(new IDynamicNettyTransportSessionFactory() {
            @Override
            public IDynamicTransportSession get () {
                return customRequestHandler;
            }

            @Override
            public String getName () {
                return customRequestHandler.getName();
            }

            @Override
            public DynamicTransportSessionAddPosition addAt () {
                return customRequestHandler.addAt();
            }
        });
    }

    public void addCustomRequestHandlerFactory (final IDynamicNettyTransportSessionFactory
            customRequestHandlingTransportSessionFactory) {
        // lazy initialization is used so as to avoid an extra flag
        if (this.customRequestHandlers == null) {
            this.customRequestHandlers = new ArrayList<IDynamicNettyTransportSessionFactory>();
        }
        logger.trace("adding custom request handler factory");
        this.customRequestHandlers.add(customRequestHandlingTransportSessionFactory);
    }


    /**
     * Getter for @customRequestHandlers
     *
     * @return
     */
    public ArrayList<IDynamicNettyTransportSessionFactory> getCustomRequestHandlers () {
        return this.customRequestHandlers;
    }


    /**
     * Method to add @SocketIoServerNamespace to the configuration
     *
     * @param namespace
     * @return
     */
    public SocketIoServerConfig addNamespace (final SocketIoServerNamespace namespace) {
        this.namespaces.add(namespace);
        return this;

    }


    /**
     * Factory
     *
     * @return instance of @SocketIoServerConfig
     */
    public static SocketIoServerConfig get () {
        return new SocketIoServerConfig();
    }

}
