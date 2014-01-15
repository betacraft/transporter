package com.rc.transporter.core;

/**
 * Transport session interface for client connection via server
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:41 AM
 */
public interface ITransportOutgoingSession<I, O> extends ITransportIncomingSession<I, O> {

    /**
     * Called when recovery of the outgoing session starts if autorecover is opted
     */
    public void onRecoveryStarted ();

    /**
     * Called when recovery is done
     *
     * @param channel new @ITransportChannel associated with renewed connection
     */
    public void onRecovered (ITransportChannel<O> channel);

    /**
     * Called when all the recovery attempts are failed
     */
    public void onRecoveryFailed ();

}
