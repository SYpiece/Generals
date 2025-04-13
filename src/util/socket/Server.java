package util.socket;

import java.util.List;

public interface Server<L, T extends Message> extends Listenable<L, T>, AutoCloseable {
    ServerState getServerState();

    List<? extends Connection<T>> getConnections();

    enum ServerState {
        Preparing, Listened, Running, Closed
    }
}
