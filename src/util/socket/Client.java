package util.socket;

public interface Client extends Handleable, Connectable<Message>, AutoCloseable {
    ClientState getClientState();

    Connection<Message> getConnection();

    enum ClientState {
        Preparing, Connected, Running, Closed
    }
}
