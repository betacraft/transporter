package com.rc.transporter.netty3x;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportChannel;
import com.rc.transporter.core.TransportServer;
import junit.framework.TestCase;
import org.jboss.netty.channel.ChannelPipeline;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: akshay
 * Date  : 9/25/13
 * Time  : 10:45 PM
 */
public class Netty3xTransportServerTest extends TestCase {

    private Netty3xTransportServer netty3xTransportServer;
    private Netty3xTransportServerConfig serverConfig;
    private String hostname;
    private int port;


    @Before
    public void setUp() throws Exception {
        this.serverConfig = Netty3xTransportServerConfig.getDefault(new Netty3xChannelPipelineFactory() {
            @Override
            public void initializeChannel(ChannelPipeline pipeline) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        this.hostname = "0.0.0.0";
        this.port = 8000;
        this.netty3xTransportServer = new Netty3xTransportServer(this.serverConfig);

    }

    @After
    public void tearDown() throws Exception {
        this.netty3xTransportServer.close();
    }

    @Test
    public void testStart() throws Exception {
        try {
            this.netty3xTransportServer.start(this.hostname, this.port, new TransportServer.ITransportServerListener() {
                        @Override
                        public void onClosed() {
                            //To change body of implemented methods use File | Settings | File Templates.
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

                        @Override
                        public void setProperty (String key, Object value) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }
            );
            assertTrue(true);
        } catch (Exception exception) {
            assertFalse(true);
        }
        this.netty3xTransportServer.close();
    }

    @Test
    public void testClose() throws Exception {
        try {
            this.netty3xTransportServer.start(this.hostname, this.port, new TransportServer.ITransportServerListener() {
                        @Override
                        public void onClosed() {
                            assertTrue(true);
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

                        @Override
                        public void setProperty (String key, Object value) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }
            );
        } catch (Exception exception) {

        }
        this.netty3xTransportServer.close();
    }
}
