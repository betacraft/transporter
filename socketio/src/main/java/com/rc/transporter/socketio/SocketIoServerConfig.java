package com.rc.transporter.socketio;

import com.corundumstudio.socketio.Configuration;
import com.rc.transporter.core.ITransportSession;
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
    private ArrayList<ITransportSession> customRequestHandlers;
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
     * Add custom request hanlder
     *
     * @param customRequestHandler
     */
    public void addCustomRequestHandler (final ITransportSession customRequestHandler) {
        // lazy initialization is used so as to avoid an extra flag
        if (this.customRequestHandlers == null) {
            this.customRequestHandlers = new ArrayList<ITransportSession>();
        }
        this.customRequestHandlers.add(customRequestHandler);

    }

    /**
     * Getter for @customRequestHandlers
     *
     * @return
     */
    public ArrayList<ITransportSession> getCustomRequestHandlers () {
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
