package com.rc.transported.rawsocket;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Raw transport server
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 11:59 PM
 */
public final class RawTransportServer extends TransportServer {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RawTransportServer.class);
    /**
     * Executors for this server
     */
    private ExecutorService transportServerWorkers = Executors.newCachedThreadPool();
    /**
     * Server socket for this server
     */
    private ServerSocket serverSocket;
    /**
     * Keep running flag
     */
    private AtomicBoolean keepRunning = new AtomicBoolean(true);

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
        this.transportServerWorkers.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(port), 200);
                    while (keepRunning.get()) {
                        final Socket socket = serverSocket.accept();
                        transportServerWorkers.submit(new Runnable() {
                            @Override
                            public void run() {
                                new RawTransportSession(socket, transportSession);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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
    public void start(final String hostname, final int port,
                      final ITransportServerListener transportServerListener, final ITransportSession transportSession)
            throws Exception {
        this.transportServerWorkers.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(hostname, port), 200);
                    while (keepRunning.get()) {
                        final Socket socket = serverSocket.accept();
                        transportServerWorkers.submit(new Runnable() {
                            @Override
                            public void run() {
                                new RawTransportSession(socket, transportSession);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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
        this.transportServerWorkers.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(port), 200);
                    while (keepRunning.get()) {
                        final Socket socket = serverSocket.accept();
                        transportServerWorkers.submit(new Runnable() {
                            @Override
                            public void run() {
                                new RawTransportSession(socket, transportSessionFactory.get());
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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
    public void start(final String hostname, final int port,
                      final ITransportServerListener transportServerListener,
                      final ITransportSessionFactory transportSessionFactory) throws Exception {
        this.transportServerWorkers.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(hostname, port), 200);

                    while (keepRunning.get()) {
                        final Socket socket = serverSocket.accept();
                        transportServerWorkers.submit(new Runnable() {
                            @Override
                            public void run() {
                                new RawTransportSession(socket, transportSessionFactory.get());
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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
        if (!this.keepRunning.get())
            return;
        this.keepRunning.set(false);
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.transportServerWorkers.shutdownNow();
    }
}
