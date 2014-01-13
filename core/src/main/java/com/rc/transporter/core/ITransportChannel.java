package com.rc.transporter.core;

/**
 * Interface for transport channel
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:56 AM
 */
public interface ITransportChannel<M> {

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    public abstract void sendData (M data);

    /**
     * Method to push data with promise
     *
     * @param data
     * @param callback
     */
    public abstract void sendData (M data, ITransportCallback callback);

    /**
     * Method to close a channel
     */
    public abstract void close ();

    /**
     * Method to check if channel is open
     */
    public abstract boolean isOpen ();

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    public abstract void setProperty (final String name, Object value);

}
