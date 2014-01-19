package com.rc.transporter.socketio;

import com.rc.transporter.core.ITransportSession;
import com.rc.transporter.core.TransportChannel;
import com.rc.transporter.core.TransportServer;
import com.rc.transporter.netty4x.DynamicTransportSession;
import com.rc.transporter.netty4x.DynamicTransportSessionAddPosition;
import com.rc.transporter.netty4x.IDynamicNettyTransportSessionFactory;
import com.rc.transporter.netty4x.IDynamicTransportSession;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
       /* socketIoServerConfig.getConfiguration().setKeyStore(new FileInputStream(new File
                ("/home/akshay/code_base/appsurfer-java-node/keystore/appsurfer-java-node-keystore.keystore")
        ));
        socketIoServerConfig.getConfiguration
                ().setKeyStorePassword("appsurfer1511"); */
        socketIoServerConfig.addCustomRequestHandlerFactory(new IDynamicNettyTransportSessionFactory() {
            @Override
            public IDynamicTransportSession get () {
                return new DynamicTransportSession() {

                    TransportChannel channel;

                    @Override
                    public void onConnected (final TransportChannel channel) {
                        logger.trace("commet Got connection");
                        this.channel = channel;

                    }

                    @Override
                    public void onDisconnected () {
                        logger.trace("commet disconnected");
                    }

                    @Override
                    public void onError (Throwable cause) {
                        logger.trace("commet on error", cause);
                    }

                    @Override
                    public void onData (Object data) {
                        logger.trace("Got data " + data.toString());
                    }

                    @Override
                    public void setProperty (String key, Object value) {

                    }

                    @Override
                    public String getName () {
                        return "test";
                    }

                    @Override
                    public boolean validate (Object data) {
                        FullHttpRequest request = (FullHttpRequest) data;
                        if (request.getUri().contains("/crossdomain.xml")){
                            writeHeader(request);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean isStandalone () {
                        return false;
                    }


                    @Override
                    public DynamicTransportSessionAddPosition addAt () {
                        return DynamicTransportSessionAddPosition.ADD_LAST;
                    }

                    private final String responseBuffer = ""
                            + "<?xml version=\"1.0\" ?>\n"
                            + "<!DOCTYPE cross-domain-policy SYSTEM 'http://www.macromedia.com/xml/dtds/cross-domain-policy" +
                            ".dtd'>\n"
                            + "<cross-domain-policy>\n"
                            + "    <allow-access-from domain=\"*\" to-ports=\"*\" secure=\"true\"/>"
                            + "    <allow-access-from domain='d2xclp3ege6hxd.cloudfront.net' to-ports='*' />\n"
                            + "    <allow-access-from domain='da1e79qj82tlx.cloudfront.net' to-ports='*' />\n"
                            + "</cross-domain-policy>";

                    private void writeHeader (HttpRequest request) {
                        boolean keepAlive = isKeepAlive(request);
                        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer
                                (responseBuffer, CharsetUtil.UTF_8));
                        response.headers().set(CONTENT_TYPE, "text/event-stream");
                        response.headers().set(CONTENT_ENCODING, HttpHeaders.Values.CHUNKED);
                        response.headers().set(TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
                        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, " +
                                "Accept");

                        if (keepAlive) {
                            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
                            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                        }

                        // Write the response.
                        this.channel.sendData(response);
                    }

                };  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getName () {
                return "test";  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public DynamicTransportSessionAddPosition addAt () {
                return DynamicTransportSessionAddPosition.ADD_LAST;
            }


        });
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
