package com.rc.transporter.netty4x;

/**
 * Author: akshay
 * Date  : 1/12/14
 * Time  : 3:14 AM
 */
public interface NettyOutgoingChannelStateListener {
    public void onSessionDropped ();

    public void onSessionDropped (final Throwable cause);
}
