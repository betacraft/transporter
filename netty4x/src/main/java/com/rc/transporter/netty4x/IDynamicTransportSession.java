package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportSession;

/**
 * Author: akshay
 * Date  : 11/27/13
 * Time  : 1:03 AM
 */
public interface IDynamicTransportSession<I, O> extends ITransportSession<I, O> {

    public String getName ();

    public boolean validate (I data);

}
