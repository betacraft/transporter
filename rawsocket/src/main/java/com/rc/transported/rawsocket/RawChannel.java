package com.rc.transported.rawsocket;

import com.rc.transporter.core.TransportChannel;

import java.net.Socket;

/**
 * Transport channel associated with raw socket server
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 4:56 PM
 */
public class RawChannel<M> extends TransportChannel<M> {

    /**
     * Underlying socket associated with this channel
     */
    private Socket socket;


    /**
     * Constructor
     *
     * @param socket @Socket associated with this channel
     */
    public RawChannel(final Socket socket) {
        this.socket = socket;
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData(M data) {

    }

    @Override
    protected void closeChannel() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    @Override
    public void setProperty(String name, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
