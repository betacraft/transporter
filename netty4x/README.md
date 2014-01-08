# Netty4x

This module implements Transporter API's using Netty4x versions. Transporter APIs are influenced by Netty APIs itself so this part is straightforward.


# Changes in architecture

DynamicNettyTransport session should be lifted up and added into core APIs. As all the modules except raw sockets use pipeline system, which can be easily added to that. Will be coding theses changes as time permits.

If anybody is interested contact me :).