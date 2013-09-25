package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportChannel;
import com.rc.transporter.core.TransportServer;
import com.rc.transporter.core.TransportSession;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: akshay
 * Date  : 9/25/13
 * Time  : 11:33 PM
 */
public class Netty3xTransportClientTest extends TestCase {

    private Netty3xTransportServer server;
    private Netty3xTransportClient client;

    @Before
    public void setUp() throws Exception {
        this.server = new Netty3xTransportServer(Netty3xTransportServerConfig.getDefault());
        this.server.start("0.0.0.0", 8000, new TransportServer.ITransportServerListener() {
                    @Override
                    public void onClosed() {
                    }
                }, new ITransportSession() {
                    @Override
                    public void onConnected(TransportChannel channel) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onDisconnected() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onError(Throwable cause) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onData(Object data) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        );

        this.client = new Netty3xTransportClient(Netty3xTransportClientConfig.getDefault());

    }

    @After
    public void tearDown() throws Exception {
        this.server.close();
        this.client.close();
    }

    @Test
    public void testConnect() throws Exception {
        try {
            this.client.connect("0.0.0.0", 8000, new TransportSession() {
                @Override
                public void onDisconnected() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onError(Throwable cause) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onData(Object data) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            assertTrue(true);
        } catch (Exception exception) {
            assertFalse(true);
        }
        this.client.close();
    }

    @Test
    public void testClose() throws Exception {
        try {
            this.client.connect("0.0.0.0", 8000, new TransportSession() {
                @Override
                public void onDisconnected() {
                    assertTrue(true);
                }

                @Override
                public void onError(Throwable cause) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onData(Object data) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        } catch (Exception exception) {
            assertFalse(true);
        }
        this.client.close();

        assertTrue(true);
    }
}
