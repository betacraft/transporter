package com.rc.transported.rawsocket;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;

/**
 * Raw transport server
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 11:59 PM
 */
public final class RawTransportServer extends TransportServer {


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
    public void start(int port, ITransportServerListener transportServerListener, ITransportSession transportSession) throws Exception {

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
    public void start(String hostname, int port, ITransportServerListener transportServerListener, ITransportSession transportSession) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
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
    public void start(int port, ITransportServerListener transportServerListener, ITransportSessionFactory transportSessionFactory) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
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
    public void start(String hostname, int port, ITransportServerListener transportServerListener, ITransportSessionFactory transportSessionFactory) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Method to close server
     */
    @Override
    protected void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}