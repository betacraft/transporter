package com.rc.transporter.netty4x;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Netty transport system
 * Author: akshay
 * Date  : 9/21/13
 * Time  : 3:44 AM
 */
public class NettyWebsocketTransportSession<I, O> extends SimpleChannelInboundHandler<Object>
        implements WebsocketSession {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyWebsocketTransportSession.class);
    /**
     * @NettyChannel associated with this session
     */
    private NettyChannel<O> nettyChannel;
    /**
     * Underlying @ITransportSession
     */
    private INettyWebsocketTransportSession<I, O> transportSession;
    /**
     * Is ssl request
     */
    private boolean isSsl = false;
    /**
     * Requst path
     */
    private String path;
    /**
     * Current http parameters
     */
    private Map<String, List<String>> parameters;

    /**
     * Websocket version
     */
    private WebSocketVersion webSocketVersion;
    /**
     * Is session validataed
     */
    private AtomicBoolean isSessionValidated = new AtomicBoolean(false);

    /**
     * Default constructor
     */
    public NettyWebsocketTransportSession () {

    }


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.debug("Channel connected");
        this.nettyChannel = new NettyChannel<O>(ctx);
    }

    /**
     * Constructor
     *
     * @param transportSession @ITransportSession which receives event
     */
    public NettyWebsocketTransportSession (INettyWebsocketTransportSession<I,
            O> transportSession) {
        this.transportSession = transportSession;
    }

    /**
     * @param ctx the {@link io.netty.channel.ChannelHandlerContext} which this {@link io.netty.channel
     *            .SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void channelRead0 (final ChannelHandlerContext ctx, final Object msg) throws Exception {

        if (msg instanceof CloseWebSocketFrame) {
            ((CloseWebSocketFrame) msg).retain();
            ctx.channel().close();
            ((CloseWebSocketFrame) msg).release();
        } else if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            frame.retain();
            transportSession.onData((I) frame.text());
            frame.release();
        } else if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.getUri());
            this.path = queryStringDecoder.path();
            this.parameters = queryStringDecoder.parameters();
            this.isSsl = req.getUri().contains("https");
            if (!transportSession.validateRequestPath(this.path)) {
                ctx.close();
                return;
            }
            this.isSessionValidated.set(true);
            logger.debug(req.toString());
            if (this.isSsl) logger.trace("wss");
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false);
            WebSocketServerHandshaker handshaker = factory.newHandshaker(req);
            if (handshaker != null) {
                this.webSocketVersion = handshaker.version();
                handshaker.handshake(ctx.channel(), req).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete (ChannelFuture future) throws Exception {
                        transportSession.onConnected(NettyWebsocketTransportSession.this,
                                nettyChannel);
                    }
                });
            } else {
                logger.error("Unsupported socket version exception");
                HttpResponse res = new DefaultHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.UPGRADE_REQUIRED);
                res.headers().set(HttpHeaders.Names.SEC_WEBSOCKET_VERSION,
                        WebSocketVersion.V13.toHttpHeaderValue());
                ctx.channel().writeAndFlush(res).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete (ChannelFuture future) throws Exception {
                        ctx.channel().close();
                    }
                });
            }
        }
    }

    /**
     * Method to get websocket location
     *
     * @param req
     * @return
     */
    private String getWebSocketLocation (final HttpRequest req) {
        String protocol = "ws://";
        if (this.isSsl) {
            protocol = "wss://";
        }
        return protocol + req.headers().get(HttpHeaders.Names.HOST) + req.getUri();
    }


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void userEventTriggered (ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

    }


    /**
     * Method to get all parameters
     *
     * @param name
     * @return
     */
    @Override
    public List<String> getParameters (final String name) {
        if (hasParameter(name)) {
            return this.parameters.get(name);
        }
        return null;
    }


    /**
     * Method to get a parameter
     *
     * @param name
     * @return
     */
    @Override
    public String getParameter (final String name) {
        if (hasParameter(name)) {
            return this.parameters.get(name).get(0);
        }
        return null;
    }

    @Override
    public String getPath () {
        return this.path;
    }

    /**
     * Method to check if req has a param or not
     *
     * @param name
     * @return
     */
    @Override
    public boolean hasParameter (final String name) {
        return this.parameters.containsKey(name);
    }

    @Override
    public WebSocketVersion getWebSocketVersion () {
        return this.webSocketVersion;
    }


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel
     * .ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelInactive (ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.isSessionValidated.get())
            transportSession.onDisconnected();
        logger.info("Channel inactive " + ctx.name());
        if (this.nettyChannel != null)
            this.nettyChannel.close();
    }


    /**
     * Calls {@link io.netty.channel.ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link io.netty.channel.ChannelHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Exception caught ", cause);
        if (this.isSessionValidated.get())
            transportSession.onError(cause);
        if (this.nettyChannel != null)
            this.nettyChannel.closeChannel();
    }
}
