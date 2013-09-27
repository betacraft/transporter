package com.rc.transported.rawsocket;

import com.rc.transporter.core.ITransportSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ITransportSession transportSession;
    /**
     * Executor associated with this session
     */
    private ExecutorService sessionExecutor = Executors.newSingleThreadExecutor();


    private RawChannel channel;


    public RawTransportSession(final Socket socket) {
        this.channel  = new RawChannel(socket);
    }


}
