package util.socket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public interface Connectable<T extends Message> {
    Connection<T> connect(SocketAddress socketAddress) throws IOException;

    <A> Future<Connection<T>> connect(SocketAddress socketAddress, A attachment, CompletionHandler<Connection<T>, A> handler);
}
