package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.rc.transporter.core.TransportChannel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Socketio channel associated with a session
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 6:31 PM
 */
public final class SocketIoChannel extends TransportChannel<String> {


    /**
     * Socket client
     */
    private SocketIOClient socketIOClient;
    /**
     * Flag for close
     */
    private AtomicBoolean isClosed = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param socketIOClient @SocketIOClient socket io client
     */
    public SocketIoChannel(final SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData(String data) {
        this.socketIOClient.sendMessage(data);
    }


    public void sendEvent(String eventName, Object data) {
        this.socketIOClient.sendEvent(eventName, data);
    }

    @Override
    protected void closeChannel() {
        if (this.isClosed.get())
            return;
        this.isClosed.set(true);
        this.socketIOClient.disconnect();
    }
}
