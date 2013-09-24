## Transporter

Transporter library allows to keep your business logic separate from the underlying communication framework
being used in your system components. So it doesn't matter if you are using  raw sockets, netty4.x, netty3.x, socketio
or websockets. Transporter is designed in a way that adding or switching between these frameworks is less painful and
is with minimum code hit.

This library is in it's nascent phases and being actively developed in RainingClouds.


## Architecture

```
                                              +------------+
                               |              | raw socket |
                               |              +------------+
    +------------+             |              | netty 3.x  |
    | Your       |      +------+------+       +------------+
    | Business   |  <-> | Transporter |  <->  | netty 4.x  |
    | Logic      |      +------+------+       +------------+
    +------------+             |              |  socketio  |
                               |              +------------+
                               |              |    ...     |
```

So ideally this library acts as an interface between your business logic and transport layer. Right now netty4x
implementation is available and soon netty3x will be pushed.

## Components


#### TransportServer

Contract for writing transport server

```
public abstract class TransportServer {
    /**
     * Transport server listener to listen various events related with server lifecycle
     */
    public interface ITransportServerListener {
        /**
         * Callback called when server is closed
         */
        public void onClosed();
    }

    /**
     * Constructor for the server with shutdown hook to cleanup properly before process shutsdown
     */
    protected TransportServer() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }));
    }

    /**
     * Method to start server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param transportSession        @ITransportSession session routine that will be associated with each connection received
     *                                on this server
     * @throws Exception throws exception if any during starting the server
     */
    public abstract void start(final String hostname, final int port,
                               final ITransportServerListener transportServerListener, final ITransportSession transportSession)
            throws Exception;

    /**
     * Method to close server
     */
    protected abstract void close();


}
```
Sample netty4x server:
(for details refer netty4x project)
```
public final class NettyTransportServer implements ITransportServer {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyTransportServer.class);
    /**
     * Server config
     */
    private NettyTransportServerConfig serverConfig;
    /**
     * Constructor
     *
     * @param config netty server config
     */
    public NettyTransportServer(final NettyTransportServerConfig config) {
        this.serverConfig = config;
    }


    /**
     * Starting server
     *
     * @param hostname                hostname
     * @param port                    port on which server needs to be started
     * @param transportServerListener @ITransportServerListener listener to listen the state of the server
     * @param nettyTransportSession   @ITransportSession associated with each connection being received on this server
     */
    @Override
    public void start(final String hostname, final int port, final ITransportServerListener transportServerListener,
                      final ITransportSession nettyTransportSession) {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.serverConfig.getBossGroup(), this.serverConfig.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        /**
                         * This method will be called once the {@link io.netty.channel.Channel} was registered. After the method returns this instance
                         * will be removed from the {@link io.netty.channel.ChannelPipeline} of the {@link io.netty.channel.Channel}.
                         *
                         * @param ch the {@link io.netty.channel.Channel} which was registered.
                         * @throws Exception is thrown if an error occours. In that case the {@link io.netty.channel.Channel} will be closed.
                         */
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            for (Map.Entry<String, ChannelHandler> pipelineEntry : serverConfig.getPipeline().entrySet()) {
                                pipeline.addLast(pipelineEntry.getKey(), pipelineEntry.getValue());
                            }
                            pipeline.addLast("handler", new NettyTransportSession(nettyTransportSession));
                        }
                    });

            // setting up options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChannelOptions().entrySet()) {
                serverBootstrap.option(entry.getKey(), entry.getValue());
            }
            // setting up child options
            for (Map.Entry<ChannelOption, Object> entry : this.serverConfig.getChildChannelOptions().entrySet()) {
                serverBootstrap.childOption(entry.getKey(), entry.getValue());
            }
            // bind server
            serverBootstrap.bind(hostname, port).sync().channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    transportServerListener.onClosed();
                }
            });
        } catch (ChannelException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.serverConfig.getBossGroup().shutdownGracefully();
            this.serverConfig.getWorkerGroup().shutdownGracefully();
        }
    }
}
```

#### ITransportClient

Contract for writing client.

```
public interface ITransportClient<M> {
    /**
     * Method to initialize connection with @ITransportServer or other server
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     */
    public void connect(final String host, final int port, TransportSession<M> transportSession);
}
```

#### ITransportSession

Contract for associating with a connection.

```
public interface ITransportSession<M> {

    /**
     * Callback called when connection is received in session
     *
     * @param channel @TransportChannel associated with this session
     */
    public void onConnected(TransportChannel channel);

    /**
     * Callback called when connection is disconnected
     */
    public void onDisconnected();

    /**
     * Callback called when error is caused in session
     *
     * @param cause Error cause
     */
    public void onError(Throwable cause);

    /**
     * Callback called when data is available
     *
     * @param data data
     */
    public void onData(M data);
}

```

### Info

For any other help or info, you can mail us at akshay@rainingclouds.com.