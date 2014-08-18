package com.rc.transporter.netty4x;

import com.rc.transporter.core.ITransportSession;

/**
 * Author: akshay
 * Date  : 11/27/13
 * Time  : 1:03 AM
 */
public interface IDynamicTransportSession<I, O> extends ITransportSession<I, O> {
    /**
     * Name of the session
     * this name will be associated with the Handler if applicable
     *
     * @return
     */
    public String getName ();

    /**
     * Method that validates session based on the first data
     *
     * @param data
     * @return
     */
    public boolean validate (I data);

    /**
     * If this is true all the other handlers are removed (Inbound) if applicable
     *
     * @return
     */
    public boolean isStandalone ();

    /**
     * Where to add this session in the pipeline
     *
     * @return
     */
    public DynamicTransportSessionAddPosition addAt ();

}
