package com.rc.transporter.socketio;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportServer;

/**
 * Wrapper interface for all interfaces associated with SocketIOTransportSession
 * Author: akshay
 * Date  : 12/9/13
 * Time  : 8:45 PM
 */
public interface ISocketIOTransportSession extends ConnectListener, DisconnectListener, DataListener<String> {

    public ISocketIOTransportSession setSharableTransportSession (final ITransportSession transportSession);

    public ISocketIOTransportSession setClientSessionFactory (final TransportServer.ITransportSessionFactory
            transportSessionFactory);
}
