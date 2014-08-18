package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportChannel;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 2/10/14
 * Time  : 7:49 PM
 */
public interface INettyWebsocketTransportSession<I, O> {

    /**
     * Callback called when connection is received in session
     *
     * @param channel @TransportChannel associated with this session
     */
    public void onConnected (WebsocketSession websocketSession, TransportChannel<O> channel);

    /**
     * Callback called when connection is disconnected
     */
    public void onDisconnected ();

    /**
     * Callback called when error is caused in session
     *
     * @param cause Error cause
     */
    public void onError (Throwable cause);

    /**
     * Callback called when data is available
     *
     * @param data data
     */
    public void onData (I data);

    /**
     * Generic method for setting properties related with session
     */
    public void setProperty (final String key, final Object value);

    /**
     * Method to
     *
     * @param path
     * @return if path is valid for the session or not
     */
    public boolean validateRequestPath (final String path);


}
