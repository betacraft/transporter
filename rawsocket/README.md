## RawSocket

This transporter is based on raw sockets. Now inherently raw sockets in java are not event driven but they perform
blocking io. So crux of this transporter is to make these io bound operations event based using executors.