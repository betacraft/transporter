package com.rc.transporter.core;

/**
 * Author: akshay
 * Date  : 1/13/14
 * Time  : 9:01 PM
 */
public interface ITransportCallback {

    public void onDataSent ();

    public void onDataSendingFailed (Throwable cause);

}
