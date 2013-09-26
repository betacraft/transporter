package com.rc.transporter.socketio;

import com.rc.transporter.core.ITransportSession;

import java.util.HashMap;

/**
 * Socketio server configuration
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 6:59 PM
 */
public final class SocketIoServerConfig {
    /**
     * Socket io server session factory
     */
    public interface SocketIoServerSessionFactory {
        public ITransportSession get();
    }

    /**
     * Namespaces associated with this server
     */
    private HashMap<String, SocketIoServerSessionFactory> namespaces;

}
