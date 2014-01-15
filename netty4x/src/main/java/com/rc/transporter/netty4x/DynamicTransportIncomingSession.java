package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportIncomingSession;

/**
 * Author: akshay
 * Date  : 11/27/13
 * Time  : 1:03 AM
 */
public abstract class DynamicTransportIncomingSession<I, O> extends TransportIncomingSession<I,
        O> implements IDynamicTransportIncomingSession<I, O> {
}
