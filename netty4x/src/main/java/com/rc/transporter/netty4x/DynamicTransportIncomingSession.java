package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportIncomingSession;

/**
 * Author: akshay
 * Date  : 11/27/13
 * Time  : 1:03 AM
 */
public abstract class DynamicTransportIncomingSession<I, O>
        implements ITransportIncomingSession<I, O>, IDynamicTransportIncomingSession<I, O> {
}
