package com.rc.transporter.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.VoidAckCallback;
import com.rc.transporter.core.TransportChannel;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Socketio channel associated with a session
 * Author: akshay
 * Date  : 9/26/13
 * Time  : 6:31 PM
 */
public final class SocketIoChannel extends TransportChannel<Object> {
    /**
     * Socket client
     */
    private SocketIOClient socketIOClient;
    /**
     * Flag for close
     */
    private volatile AtomicBoolean isClosed = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param socketIOClient @SocketIOClient socket io client
     */
    public SocketIoChannel (final SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    /**
     * Method to push data on channel
     *
     * @param data data to be pushed
     */
    @Override
    public void sendData (Object data) {
        if (!this.isOpen()) {
            return;
        }
        if (data instanceof String) {
            this.socketIOClient.sendMessage((String) data);
            return;
        }
        if (data instanceof ByteBuf) {
            this.socketIOClient.sendMessage(((ByteBuf) data).toString(Charset.defaultCharset()));
            return;
        }
        throw new IllegalStateException("Unsupported data " + data.getClass().getName());

    }

    @Override
    public void sendData (final Object data, final IDataSendListener dataSendListener) {
        if (!this.isOpen()) {
            return;
        }
        if (data instanceof String) {
            this.socketIOClient.sendMessage((String) data, new VoidAckCallback() {
                @Override
                protected void onSuccess () {
                    dataSendListener.sendComplete(data);
                }
            });
            return;
        }
        if (data instanceof ByteBuf) {
            this.socketIOClient.sendMessage(((ByteBuf) data).toString(Charset.defaultCharset()),
                    new VoidAckCallback() {
                        @Override
                        protected void onSuccess () {
                            dataSendListener.sendComplete(data);
                        }
                    });
            return;
        }
        throw new IllegalStateException("Unsupported data " + data.getClass().getName());

    }


    @Override
    public void sendAndClose (Object data) {
        if (!this.isOpen())
            return;
        if (data instanceof String) {
            this.socketIOClient.sendMessage((String) data, new VoidAckCallback() {
                @Override
                protected void onSuccess () {
                    if (socketIOClient == null)
                        return;
                    socketIOClient.disconnect();
                }
            });
            return;
        }
        if (data instanceof ByteBuf) {
            this.socketIOClient.sendMessage(((ByteBuf) data).toString(Charset.defaultCharset()),
                    new VoidAckCallback() {

                        @Override
                        protected void onSuccess () {
                            if (socketIOClient == null)
                                return;
                            socketIOClient.disconnect();
                        }
                    });
            return;
        }
        throw new IllegalStateException("Unsupported data " + data.getClass().getName());
    }


    /**
     * Method to send an event over socketio
     *
     * @param eventName name of the event
     * @param data      data associated with the event
     */

    public void sendEvent (String eventName, Object data) {
        this.socketIOClient.sendEvent(eventName, data);
    }

    @Override
    protected void closeChannel () {
        if (this.isClosed.getAndSet(true))
            return;
        this.socketIOClient.disconnect();
    }

    /**
     * Method to check if channel is open
     */
    @Override
    public boolean isOpen () {
        return !this.isClosed.get();
    }

    /**
     * Method to set properties associated with channel
     *
     * @param name  name of the property
     * @param value value of the property
     */
    @Override
    public void setProperty (String name, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
