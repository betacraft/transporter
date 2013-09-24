package com.rc.transporter.core;

/**
 * Transport server interface
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 3:52 AM
 */
public interface ITransportServer {
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
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    public void start(final String hostname, final int port,
                      final ITransportServerListener transportServerListener, final ITransportSession transportSession)
            throws Exception;

}
