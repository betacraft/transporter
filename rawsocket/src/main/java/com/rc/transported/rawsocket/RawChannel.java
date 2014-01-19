package com.rc.transported.rawsocket;

import com.rc.transporter.core.TransportChannel;

import java.io.IOException;
import java.net.Socket;

/**
 * Transport channel associated with raw socket server
 * Author: akshay
 * Date  : 9/27/13
 * Time  : 4:56 PM
 */
public class RawChannel extends TransportChannel<byte[]> {

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
    public void sendData(byte[] data) {
        try {
            this.socket.getOutputStream().write(data);
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void sendData (byte[] data, IDataSendListener dataSendListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendAndClose (byte[] data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void closeChannel() {
        if (this.socket == null)
            return;
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if channel is open
     */
    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    @Override
    public void setProperty(String name, Object value) {
        throw new IllegalStateException("This method is not supported");
    }
}
