package com.rc.transporter.socketio;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.ITransportSession;

/**
 * Author: akshay
 * Date  : 10/31/13
 * Time  : 12:11 AM
 */
public class SocketIoTransportClient implements ITransportClient {
    /**
     * Method to initialize connection with @TransportServer or other server
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     */
    @Override
    public void connect(String host, int port, ITransportSession transportSession) throws Exception {

    }

    /**
     * Method to close client
     */
    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
