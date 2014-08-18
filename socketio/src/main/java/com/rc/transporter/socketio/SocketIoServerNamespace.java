package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.rc.transporter.core.TransportServer;
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
     * Namespace path
     */
    private final String path;
    /**
     * SocketIO transport session factory
     */
    private final SocketIOTransportSessionFactory socketIOTransportSessionFactory;


    /**
     * Constructor
     *
     * @param path                 path of the namespace
     * @param clientSessionFactory @SocketIoServerSessionFactory for this namespace
     */
    private SocketIoServerNamespace (final String path, final TransportServer.ITransportSessionFactory
            clientSessionFactory) {
        this.path = path;
        this.socketIOTransportSessionFactory = new SocketIOTransportSessionFactory() {
            @Override
            public ISocketIOTransportSession get () {
                return new SocketIoTransportSession().setClientSessionFactory(clientSessionFactory);
            }
        };
    }

    /**
     * Constructor
     *
     * @param path
     * @param socketIOTransportSessionFactory
     *
     */
    private SocketIoServerNamespace (final String path,
            final SocketIOTransportSessionFactory socketIOTransportSessionFactory) {
        this.path = path;

        this.socketIOTransportSessionFactory = socketIOTransportSessionFactory;
    }

    /**
     * Method
     *
     * @param socketIOServer
     */
    protected void setOn (final SocketIOServer socketIOServer) {
        logger.trace("Setting namespace {}", path);
        SocketIONamespace namespace = socketIOServer.addNamespace(this.path);
        ISocketIOTransportSession session = this.socketIOTransportSessionFactory.get();
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
    public static SocketIoServerNamespace get (final String path,
            final TransportServer.ITransportSessionFactory clientSessionFactory) {
        return new SocketIoServerNamespace(path, clientSessionFactory);
    }

    /**
     * Factory
     *
     * @param path path of the namespace
     * @return
     */
    public static SocketIoServerNamespace get (final String path,
            final SocketIOTransportSessionFactory socketIOTransportSessionFactory) {
        return new SocketIoServerNamespace(path, socketIOTransportSessionFactory);
    }

}
