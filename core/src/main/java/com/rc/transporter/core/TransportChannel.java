package com.rc.transporter.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for transport channel
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:56 AM
 */
public abstract class TransportChannel<M> {
    /**
     * Logger
     */
    protected final static Logger logger = LoggerFactory.getLogger(TransportChannel.class);


    /**
     * Channel state listener which is used internally
     */
    interface IChannelStateListener {
        void onClose ();
    }

    /**
     * Channel state listener associated with this channel
     */
    private IChannelStateListener channelStateListener;

    /**
     * Method to set channel state listener
     *
     * @param channelStateListener instance of @IChannelStateListener
     */
    void setChannelStateListener (final IChannelStateListener channelStateListener) {
        this.channelStateListener = channelStateListener;
    }


    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    public abstract void sendData (M data);


    /**
     * Method to close a channel
     */
    protected abstract void closeChannel ();

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

    /**
     * Method to close channel
     */
    public void close () {
        closeChannel();
        if (this.channelStateListener == null)
            return;
        this.channelStateListener.onClose();
    }
}
