package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Socket io server
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 6:55 PM
 */
public final class SocketIoTransportServer extends TransportServer {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIoTransportServer.class);
    /**
     * Transport server configuration
     */
    private SocketIoServerConfig transportServerConfig;
    /**
     * SocketIO server worker
     */
    private ExecutorService socketIoServerWorker = Executors.newSingleThreadExecutor();
    /**
     * Socketio server associated with this server
     */
    private SocketIOServer socketIOServer;

    /**
     * Constructor
     *
     * @param transportServerConfig @SocketIoServerConfig
     */
    public SocketIoTransportServer(final SocketIoServerConfig transportServerConfig) {
        this.transportServerConfig = transportServerConfig;
    }

    /**
     * Method to start server
     *
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession transportSession) throws Exception {
        this.socketIoServerWorker.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("Starting socketio server on port " + port);
                    transportServerConfig.getConfiguration().setPort(port);
                    socketIOServer = new SocketIOServer(transportServerConfig.getConfiguration());
                    // setting all specified namespaces
                    for (SocketIoServerNamespace socketIoServerNamespace : transportServerConfig.getNamespaces())
                        socketIoServerNamespace.setOn(socketIOServer);
                    // if on default namespace user wants a listener
                    if (transportSession != null) {
                        logger.debug("Setting default namespace listener");
                        SocketIoTransportSession defaultNamespaceSession = new SocketIoTransportSession(transportSession);
                        socketIOServer.addConnectListener(defaultNamespaceSession);
                        socketIOServer.addMessageListener(defaultNamespaceSession);
                        socketIOServer.addDisconnectListener(defaultNamespaceSession);
                        socketIOServer.addListeners(defaultNamespaceSession);
                    }
                    socketIOServer.start(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            transportServerListener.onClosed();
                        }
                    });
                } catch (Exception e) {
                    logger.error("Error ", e);
                    transportServerListener.onClosed();
                }
            }
        });
    }

    /**
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final String hostname, final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession transportSession) throws Exception {
        this.socketIoServerWorker.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("Starting socketio server on port " + port);
                    transportServerConfig.getConfiguration().setHostname(hostname);
                    transportServerConfig.getConfiguration().setPort(port);
                    socketIOServer = new SocketIOServer(transportServerConfig.getConfiguration());
                    // setting all specified namespaces
                    for (SocketIoServerNamespace socketIoServerNamespace : transportServerConfig.getNamespaces())
                        socketIoServerNamespace.setOn(socketIOServer);
                    // if on default namespace user wants a listener
                    if (transportSession != null) {
                        logger.debug("Setting default namespace listener");
                        SocketIoTransportSession defaultNamespaceSession = new SocketIoTransportSession(transportSession);
                        socketIOServer.addConnectListener(defaultNamespaceSession);
                        socketIOServer.addMessageListener(defaultNamespaceSession);
                        socketIOServer.addDisconnectListener(defaultNamespaceSession);
                        socketIOServer.addListeners(defaultNamespaceSession);
                    }
                    socketIOServer.start(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            transportServerListener.onClosed();
                        }
                    });
                } catch (Exception e) {
                    logger.error("Error ", e);
                    transportServerListener.onClosed();
                }
            }
        });
    }

    /**
     * Method to start server
     *
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSessionFactory @ITransportSessionFactory session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final int port, final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        this.socketIoServerWorker.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("Starting socketio server on port " + port);
                    transportServerConfig.getConfiguration().setPort(port);
                    socketIOServer = new SocketIOServer(transportServerConfig.getConfiguration());
                    // setting all specified namespaces
                    for (SocketIoServerNamespace socketIoServerNamespace : transportServerConfig.getNamespaces())
                        socketIoServerNamespace.setOn(socketIOServer);
                    // if on default namespace user wants a listener
                    if (transportSessionFactory != null) {
                        logger.debug("Setting default namespace listener");
                        SocketIoTransportSession defaultNamespaceSession = new SocketIoTransportSession(transportSessionFactory);
                        socketIOServer.addConnectListener(defaultNamespaceSession);
                        socketIOServer.addMessageListener(defaultNamespaceSession);
                        socketIOServer.addDisconnectListener(defaultNamespaceSession);
                        socketIOServer.addListeners(defaultNamespaceSession);
                    }
                    socketIOServer.start(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            transportServerListener.onClosed();
                        }
                    });
                } catch (Exception e) {
                    logger.error("Error ", e);
                    transportServerListener.onClosed();
                }
            }
        });
    }

    /**
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSessionFactory @ITransportSessionFactory factory for session associated with server connections
     * @throws Exception throws exception if any during starting the server
     */
    @Override
    public void start(final String hostname,
                      final int port, final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        this.socketIoServerWorker.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("Starting socketio server on port " + port);
                    transportServerConfig.getConfiguration().setHostname(hostname);
                    transportServerConfig.getConfiguration().setPort(port);
                    socketIOServer = new SocketIOServer(transportServerConfig.getConfiguration());
                    // setting all specified namespaces
                    for (SocketIoServerNamespace socketIoServerNamespace : transportServerConfig.getNamespaces())
                        socketIoServerNamespace.setOn(socketIOServer);
                    // if on default namespace user wants a listener
                    if (transportSessionFactory != null) {
                        logger.debug("Setting default namespace listener");
                        SocketIoTransportSession defaultNamespaceSession = new SocketIoTransportSession(transportSessionFactory);
                        socketIOServer.addConnectListener(defaultNamespaceSession);
                        socketIOServer.addMessageListener(defaultNamespaceSession);
                        socketIOServer.addDisconnectListener(defaultNamespaceSession);
                        socketIOServer.addListeners(defaultNamespaceSession);
                    }
                    socketIOServer.start(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            transportServerListener.onClosed();
                        }
                    });
                } catch (Exception e) {
                    logger.error("Error ", e);
                    transportServerListener.onClosed();
                }
            }
        });
    }

    /**
     * Method to close server
     */
    @Override
    protected void close() {
        if (this.socketIOServer != null)
            this.socketIOServer.stop();
        if (this.socketIoServerWorker != null)
            this.socketIoServerWorker.shutdownNow();
    }
}
