package com.rc.transporter.core;

/**
 * Transport session interface for client connection via server
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:41 AM
 */
public interface ITransportOutgoingSession<I, O> extends ITransportIncomingSession<I, O> {

    public void onRecoveryStarted ();

    public void onRecovered (TransportChannel<O> channel);

    public void onRecoveryFailed ();

}
