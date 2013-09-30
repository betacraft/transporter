package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.rc.transporter.core.ITransportSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Namespace associated with socket io server
 * Author: akshay
 * Date  : 9/30/13
 * Time  : 6:29 PM
 */
public final class SocketIoServerNamespace {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIoServerNamespace.class);

    /**
     * Socket io server session factory
     */
    public interface SocketIoServerSessionFactory {
        public ITransportSession get();
    }

    /**
     * Namespace path
     */
    private final String path;
    /**
     * Session factory for clients on this namespace
     */
    private final SocketIoServerSessionFactory clientSessionFactory;


    /**
     * Constructor
     *
     * @param path                 path of the namespace
     * @param clientSessionFactory @SocketIoServerSessionFactory for this namespace
     */
    private SocketIoServerNamespace(final String path, final SocketIoServerSessionFactory clientSessionFactory) {
        this.path = path;
        this.clientSessionFactory = clientSessionFactory;
    }

    /**
     * Method
     *
     * @param socketIOServer
     */
    void setOn(final SocketIOServer socketIOServer) {
        SocketIONamespace namespace = socketIOServer.addNamespace(this.path);
        SocketIoTransportSession session = new SocketIoTransportSession(this.clientSessionFactory);
        namespace.addConnectListener(session);
        namespace.addMessageListener(session);
        namespace.addDisconnectListener(session);
        namespace.addListeners(session);
    }

    /**
     * Factory
     *
     * @param path                 path of the namespace
     * @param clientSessionFactory @SocketIoServerSessionFactory for this namespace
     * @return
     */
    public static SocketIoServerNamespace get(final String path, final SocketIoServerSessionFactory clientSessionFactory) {
        return new SocketIoServerNamespace(path, clientSessionFactory);
    }

}
