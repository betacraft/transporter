package com.rc.transporter.socketio;

import com.corundumstudio.socketio.Configuration;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
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
    private ArrayList<TransportServer.ITransportSessionFactory> customRequestHandlers;
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
    public void addSharableCustomRequestHandler (final ITransportSession customRequestHandler) {
        // lazy initialization is used so as to avoid an extra flag
        if (this.customRequestHandlers == null) {
            this.customRequestHandlers = new ArrayList<TransportServer.ITransportSessionFactory>();
        }
        this.customRequestHandlers.add(new TransportServer.ITransportSessionFactory() {
            @Override
            public ITransportSession get () {
                return customRequestHandler;
            }
        });
    }

    public void addCustomRequestHandlerFactory (final TransportServer.ITransportSessionFactory
            customRequestHandlingTransportSessionFactory) {
        // lazy initialization is used so as to avoid an extra flag
        if (this.customRequestHandlers == null) {
            this.customRequestHandlers = new ArrayList<TransportServer.ITransportSessionFactory>();
        }
        this.customRequestHandlers.add(customRequestHandlingTransportSessionFactory);
    }


    /**
     * Getter for @customRequestHandlers
     *
     * @return
     */
    public ArrayList<TransportServer.ITransportSessionFactory> getCustomRequestHandlers () {
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
