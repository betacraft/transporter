## Socket IO based transporter server

This is the transporter server based on socket io implementation which internally uses netty.

## Complexity

SocketIO servers usually listens on a single port and has various namespaces where data is received. There is no tracking
for each session. Event is generated with SocketIOClient channel associated with this connection and data that is received.
So that thing needs to be handled at transporter end.

## Architecture

```
                                            +-------------+
                                            | Namespace 1 |
  +-----------+        +-----------+        +-------------+          +------------+
  | Transport |        | Namespace |        | Namespace 2 |          |  SocketIo  |
  | Session   | <->    | Session   |  <->   +-------------+    <->   |  Server    |
  +-----------+        | Manager   |        |     ...     |          |  Impl      |
                       +-----------+        +-------------+          +------------+
                                            | Namespace N |
                                            +-------------+
```















