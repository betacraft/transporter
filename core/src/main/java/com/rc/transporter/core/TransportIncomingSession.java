package com.rc.transporter.core;

/**
 * Transport session abstract class with some cleanup logic
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:41 AM
 */
@Deprecated
public abstract class TransportIncomingSession<I, O> implements ITransportIncomingSession<I, O> {

    /**
     * This callback ads channel state listener and does cleanup of the session if channel gets closed
     *
     * @param channel @TransportChannel associated with this session
     */
    public void onConnected (TransportChannel<O> channel) {
        throw new IllegalStateException("This class is deprecated use ITransportIncomingSession instead");
    }
}
