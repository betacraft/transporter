package com.rc.transporter.netty4x;

import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.util.List;

/**
 * Interface fo websocket session
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 2/10/14
 * Time  : 7:53 PM
 */
public interface WebsocketSession {

    public List<String> getParameters (final String name);

    public String getParameter (final String name);

    public String getPath ();

    public boolean hasParameter (final String name);

    public WebSocketVersion getWebSocketVersion();
}
