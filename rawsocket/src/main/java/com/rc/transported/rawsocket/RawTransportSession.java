package com.rc.transported.rawsocket;

import com.rc.transporter.core.ITransportSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Transport session associated with  or @RawTransportServer
 * Author: akshay
 * Date  : 9/28/13
 * Time  : 12:41 AM
 */
public final class RawTransportSession {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RawTransportSession.class);
    /**
     * Associated transport session
     */
    private ITransportSession<byte[], byte[]> transportSession;
    /**
     * Executor associated with this session
     */
    private ExecutorService sessionExecutor = Executors.newSingleThreadExecutor();
    /**
     * Channel associated with this session
     */
    private RawChannel channel;
    /**
     * Boolean to check status of the session
     */
    private AtomicBoolean keepRunning = new AtomicBoolean(true);

    /**
     * Constructor
     *
     * @param socket           @Socket associated with this session
     * @param transportSession @ITransportSession associated with this connection
     */
    public RawTransportSession(final Socket socket, final ITransportSession transportSession) {
        logger.debug("Starting raw transport session");
        this.channel = new RawChannel(socket);
        this.transportSession = transportSession;
        transportSession.onConnected(this.channel);
        // this thread will start listening onto the socket till its closed
        sessionExecutor.submit(new Runnable() {
            @Override
            public void run() {
                logger.debug("Transport session executor started");
                try {
                    BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                    byte[] data;
                    while (keepRunning.get()) {
                        if (inputStream.available() == 0) {
                            // no data available then sleep for some time and again try
                            Thread.sleep(200);
                            continue;
                        }
                        logger.debug("data available on client");
                        data = new byte[inputStream.available()];
                        inputStream.read(data);
                        transportSession.onData(data);
                    }
                    transportSession.onDisconnected();
                } catch (IOException e) {
                    logger.error("Error ", e);
                    e.printStackTrace();
                    transportSession.onDisconnected();
                } catch (InterruptedException e) {
                    logger.error("Error ", e);
                    e.printStackTrace();
                    transportSession.onError(e.getCause());
                } catch (Exception e) {
                    logger.error("Error ", e);
                    e.printStackTrace();
                    transportSession.onError(e.getCause());
                }
                closeSession();
            }
        });
    }

    /**
     * Method to close the session
     */
    void closeSession() {
        // if keep running is false then close session is already processed
        if (!keepRunning.get())
            return;
        logger.debug("closing session");
        this.keepRunning.set(false);
        this.sessionExecutor.shutdownNow();
        this.channel.close();
    }
}
