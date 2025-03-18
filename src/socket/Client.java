package socket;

import socket.event.Event;

public interface Client extends Handleable, Connectable, Readable<Event>, Writeable<Event>, AutoCloseable {
    ClientState getClientState();

    enum ClientState {
        Preparing, Connected, Running, Closed
    }
}
