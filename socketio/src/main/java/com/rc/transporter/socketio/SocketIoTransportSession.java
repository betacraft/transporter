package com.rc.transporter.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Transport session for socketio
 * Author: akshay
 * Date  : 9/30/13
 * Time  : 5:54 PM
 */
final class SocketIoTransportSession implements ConnectListener, DisconnectListener, DataListener<String> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIoTransportSession.class);
    /**
     * Connection catalog
     */
    private ConcurrentHashMap<UUID, ITransportSession> connectionCatalog;
    /**
     * Client session factory for incoming connections
     */
    private SocketIoServerNamespace.SocketIoServerSessionFactory clientSessionFactory;

    /**
     * Initialization block
     */ {
        this.connectionCatalog = new ConcurrentHashMap<UUID, ITransportSession>();
    }

    /**
     * Constructor
     *
     * @param clientSessionFactory @SocketIoServerSessionFactory
     */
    SocketIoTransportSession(final SocketIoServerNamespace.SocketIoServerSessionFactory clientSessionFactory) {
        this.clientSessionFactory = clientSessionFactory;
    }


    /**
     * Constructor
     *
     * @param transportSession sharable @ITransportSession
     */
    SocketIoTransportSession(final ITransportSession transportSession) {
        this.clientSessionFactory = new SocketIoServerNamespace.SocketIoServerSessionFactory() {
            @Override
            public ITransportSession get() {
                return transportSession;
            }
        };
    }


    /**
     * Constructor
     *
     * @param transportSessionFactory @ITransportSessionFactory
     */
    SocketIoTransportSession(final TransportServer.ITransportSessionFactory transportSessionFactory) {
        this.clientSessionFactory = new SocketIoServerNamespace.SocketIoServerSessionFactory() {
            @Override
            public ITransportSession get() {
                return transportSessionFactory.get();
            }
        };
    }

    @Override
    public void onConnect(final SocketIOClient client) {
        logger.trace("SocketIO server got a new connection");
        try {
            ITransportSession session = this.clientSessionFactory.get();
            this.connectionCatalog.put(client.getSessionId(), session);
            session.onConnected(new SocketIoChannel(client));
        } catch (Exception e) {
            logger.error("While processing onConnect", e);
        }
    }

    /**
     * Invokes when data object received from client
     *
     * @param client - receiver
     * @param data   - received object
     */
    @Override
    public void onData(SocketIOClient client, String data, AckRequest ackSender) {
        try {
            this.connectionCatalog.get(client.getSessionId()).onData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        logger.debug("SocketIO disconnected");
        if (!this.connectionCatalog.containsKey(client.getSessionId()))
            return;
        try {
            this.connectionCatalog.remove(client.getSessionId()).onDisconnected();
        } catch (Exception e) {
            logger.error("While processing onDisconnect", e);
        }
    }

    @OnEvent("reg")
    public void onEvent(SocketIOClient client, String data, AckRequest ackSender) {
        try {
            logger.trace("On reg event " + data);
            this.connectionCatalog.get(client.getSessionId()).onData(data);
        } catch (Exception e) {
            logger.error("While processing onEvent", e);
        }
    }


}
