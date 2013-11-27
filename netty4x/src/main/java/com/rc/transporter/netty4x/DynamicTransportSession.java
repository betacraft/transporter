package com.rc.transporter.netty4x;

import com.rc.transporter.core.TransportSession;

/**
 * Author: akshay
 * Date  : 11/27/13
 * Time  : 1:03 AM
 */
public abstract class DynamicTransportSession<I, O> extends TransportSession<I,
        O> implements IDynamicTransportSession<I, O> {
}
