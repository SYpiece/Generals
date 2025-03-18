package socket;

import socket.event.Event;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;

public interface Server extends Listenable<Client>, Handleable, Readable<Event>, Writeable<Event>, AutoCloseable {
    ServerState getServerState();

    List<? extends Client> getSubServers();

    enum ServerState {
        Preparing, Listened, Running, Closed
    }
}
