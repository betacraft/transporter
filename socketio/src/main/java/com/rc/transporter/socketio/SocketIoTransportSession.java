package com.rc.transporter.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
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
public class SocketIoTransportSession implements ISocketIOTransportSession {
    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(SocketIoTransportSession.class);
    /**
     * Connection catalog
     */
    protected ConcurrentHashMap<UUID, ITransportSession> connectionCatalog;
    /**
     * Client session factory for incoming connections
     */
    protected TransportServer.ITransportSessionFactory clientSessionFactory;
    /**
     * Catalog locks for each entry
     */
    protected ConcurrentHashMap<UUID, Mutex> connectionCatalogEntryLock;

    /**
     * Maximum hashmap checks
     */
    protected static final int MAX_HASH_MAP_CHECKS = 3;

    /**
     * Initialization block
     */ {
        this.connectionCatalog = new ConcurrentHashMap<UUID, ITransportSession>();
        this.connectionCatalogEntryLock = new ConcurrentHashMap<UUID, Mutex>();
    }


    public ISocketIOTransportSession setSharableTransportSession (final ITransportSession transportSession) {
        this.clientSessionFactory = new TransportServer.ITransportSessionFactory() {
            @Override
            public ITransportSession get () {
                return transportSession;
            }
        };
        return this;
    }

    public ISocketIOTransportSession setClientSessionFactory (final TransportServer.ITransportSessionFactory
            transportSessionFactory) {
        this.clientSessionFactory = new TransportServer.ITransportSessionFactory() {
            @Override
            public ITransportSession get () {
                return transportSessionFactory.get();
            }
        };
        return this;
    }


    @Override
    public void onConnect (final SocketIOClient client) {
        try {
            logger.trace("SocketIO server got a new connection {}:{}" + client.getNamespace(),
                    client.getSessionId());
            Mutex mutex = new Mutex();
            mutex.acquire();
            this.connectionCatalogEntryLock.put(client.getSessionId(), mutex);
            ITransportSession session = this.clientSessionFactory.get();
            this.connectionCatalog.put(client.getSessionId(), session);
            mutex.release();
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
    public void onData (final SocketIOClient client, final String data, final AckRequest ackSender) {
        try {
            int retries = 0;
            Mutex mutex = null;
            while (retries < MAX_HASH_MAP_CHECKS) {
                mutex = this.connectionCatalogEntryLock.get(client.getSessionId());
                if (mutex == null) {
                    Thread.sleep(200);
                } else {
                    break;
                }
                ++retries;
            }
            if (mutex == null) {
                logger.error("Socketio channel onData called before onConnected with data or it was " +
                        "already disconnected so disconnecting", data);
                client.disconnect();
                return;
            }
            mutex.acquire();
            retries = 0;
            while (retries < MAX_HASH_MAP_CHECKS) {
                try {
                    this.connectionCatalog.get(client.getSessionId()).onData(data);
                    break;
                } catch (Exception e) {
                    logger.error("Error while calling on data {} on {} retry", client.getSessionId(),
                            retries, e);
                    Thread.sleep(200);
                    ++retries;
                }
            }
            mutex.release();
            logger.trace("inside on data " + data);
        } catch (Exception e) {
            logger.error("Error while sending data", e);
        }
    }

    @Override
    public void onDisconnect (SocketIOClient client) {
        logger.debug("SocketIO disconnected");
        try {
            this.connectionCatalogEntryLock.remove(client.getSessionId());
        } catch (Exception ignored) {

        }
        if (!this.connectionCatalog.containsKey(client.getSessionId()))
            return;
        try {
            this.connectionCatalog.remove(client.getSessionId()).onDisconnected();
        } catch (Exception e) {
            logger.error("While processing onDisconnect", e);
        }
    }


}
