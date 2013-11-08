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
        socketIoServerConfig.getConfiguration().setAllowCustomRequests(true);
        socketIoServerConfig.addCustomRequestHandler(new ITransportSession() {
            @Override
            public void onConnected (TransportChannel channel) {
                logger.trace("Got connection");
            }

            @Override
            public void onDisconnected () {
                logger.trace("disconnected");
            }

            @Override
            public void onError (Throwable cause) {
                logger.trace("on error", cause);
            }

            @Override
            public void onData (Object data) {
                logger.trace("Got data" + data.toString());
            }
        });
        this.socketIoTransportServer = new SocketIoTransportServer(socketIoServerConfig);

    }

    public void testWithAllowCustomRequests () throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.socketIoTransportServer.start(8000, new TransportServer.ITransportServerListener() {
                    @Override
                    public void onClosed () {
                        countDownLatch.countDown();
                    }
                }, new ITransportSession() {
                    @Override
                    public void onConnected (TransportChannel channel) {
                        logger.trace("connected");
                    }

                    @Override
                    public void onDisconnected () {
                        logger.trace("on disconnected");
                    }

                    @Override
                    public void onError (Throwable cause) {
                        logger.trace("on error");
                    }

                    @Override
                    public void onData (Object data) {
                        logger.trace("On data " + data.toString());
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
