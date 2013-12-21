## Transporter

Transporter library allows to keep your business logic separate from the underlying communication framework
being used in your system components. So it doesn't matter if you are using  raw sockets, netty4.x, netty3.x, socketio
or websockets. Transporter is designed in a way that adding or switching between these frameworks is less painful and
is with minimum code hit.

This library is in it's nascent phases and being actively developed in RainingClouds.


## Architecture


                                              +------------+
                               |              | raw socket |
                               |              +------------+
    +------------+             |              | netty 3.x  |
    | Your       |      +------+------+       +------------+
    | Business   |  <-> | Transporter |  <->  | netty 4.x  |
    | Logic      |      +------+------+       +------------+
    +------------+             |              |  socketio  |
                               |              +------------+


So ideally this library acts as an interface between your business logic and transport layer.


## Supported frameworks

+ [x] Netty 4.x
+ [x] Netty 3.x
+ [x] Raw Sockets
+ [x] SocketIO
+ [ ] Java NIO (need help to complete)


## Components

#### TransportServer

Contract for writing transport server


#### ITransportClient

Contract for writing client.


#### ITransportSession

Contract for associating with a connection.

### Info

For any other help or info, you can mail us at akshay@rainingclouds.com.