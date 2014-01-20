package com.rc.transporter.socketio;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportChannel;
import com.rc.transporter.core.TransportServer;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Author: akshay
 * Date  : 11/8/13
 * Time  : 9:24 PM
 */
public class SocketIoTransportServerTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(SocketIoTransportServerTest.class);
    private SocketIoTransportServer socketIoTransportServer;

    @org.junit.Before
    public void setUp () throws Exception {
        // socketio server config
        SocketIoServerConfig socketIoServerConfig = SocketIoServerConfig.get();
        socketIoServerConfig.getConfiguration().setWorkerThreads(100);

        this.socketIoTransportServer = new SocketIoTransportServer(socketIoServerConfig);

    }

    public void testWithAllowCustomRequests () throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.socketIoTransportServer.start(8001, new TransportServer.ITransportServerListener() {
                    @Override
                    public void onClosed () {
                        countDownLatch.countDown();
                    }
                }, new ITransportSession() {
                    TransportChannel channel;

                    @Override
                    public void onConnected (TransportChannel channel) {
                        logger.trace("socketioconnected");
                        this.channel = channel;
                        this.channel.sendData("test");
                    }

                    @Override
                    public void onDisconnected () {
                        logger.trace("socketioon disconnected");
                    }

                    @Override
                    public void onError (Throwable cause) {
                        logger.trace("socketioon error");
                    }

                    @Override
                    public void onData (Object data) {
                        logger.trace("On data " + data.toString());
                        this.channel.sendData("server_test");
                    }

                    @Override
                    public void setProperty (String key, Object value) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        );
        logger.trace("Got connection");
        countDownLatch.countDown();
        assertTrue(true);
    }

    @org.junit.After
    public void tearDown () throws Exception {
        this.socketIoTransportServer.close();
    }
}
