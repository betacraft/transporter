package com.rc.transporter.core;

/**
 * Interface for transport channel
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:56 AM
 */
public abstract class TransportChannel {
    /**
     * Channel state listener which is used internally
     */
    interface IChannelStateListener {
        void onClose();
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
    void setChannelStateListener(final IChannelStateListener channelStateListener) {
        this.channelStateListener = channelStateListener;
    }


    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    public abstract void sendData(Object data);

    protected abstract void closeChannel();

    /**
     * Method to close channel
     */
    public void close() {
        closeChannel();
        if (this.channelStateListener == null)
            return;
        this.channelStateListener.onClose();
    }
}
