package com.rc.transporter.core;

/**
 * Transport session interface
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:41 AM
 */
public interface ITransportIncomingSession<I, O> {

    /**
     * Callback called when connection is received in session
     *
     * @param channel @TransportChannel associated with this session
     */
    public void onConnected (TransportChannel<O> channel);

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

}
