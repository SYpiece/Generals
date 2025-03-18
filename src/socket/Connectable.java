package socket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public interface Connectable {
    void connect(SocketAddress socketAddress) throws IOException;

    <A> Future<Void> connect(CompletionHandler<Void, A> handler, A attachment, SocketAddress socketAddress);
}
