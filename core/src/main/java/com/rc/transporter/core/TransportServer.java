package com.rc.transporter.core;

/**
 * Transport server
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 3:52 AM
 */
public abstract class TransportServer {
    /**
     * Transport server listener to listen various events related with server lifecycle
     */
    public interface ITransportServerListener {
        /**
         * Callback called when server is closed
         */
        public void onClosed();
    }

    /**
     * Constructor for the server with shutdown hook to cleanup properly before process shutsdown
     */
    protected TransportServer() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }));
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
    public abstract void start(final String hostname, final int port,
                               final ITransportServerListener transportServerListener, final ITransportSession transportSession)
            throws Exception;

    /**
     * Method to close server
     */
    protected abstract void close();
}
