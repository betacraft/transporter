package com.rc.transported.rawsocket;

import com.rc.transporter.core.ITransportClient;
import com.rc.transporter.core.ITransportSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Raw transport client
 * Author: akshay
 * Date  : 9/28/13
 * Time  : 12:42 AM
 */
public final class RawTransportClient implements ITransportClient<byte[], byte[]> {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RawTransportClient.class);
    /**
     * Raw transport session associated with this client
     */
    private RawTransportSession rawTransportSession;

    /**
     * Method to initialize connection with @TransportServer or other server
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     */
    @Override
    public void connect(final String host, final int port,
                        final ITransportSession<byte[], byte[]> transportSession) throws Exception {
        Socket socket = new Socket(host, port);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(120000); // 2 minutes
        this.rawTransportSession = new RawTransportSession(socket, transportSession);
    }

    /**
     * Method to close client
     */
    @Override
    public void close() {
        this.rawTransportSession.closeSession();
    }
}
