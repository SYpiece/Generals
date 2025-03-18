package socket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public interface Listenable<T> {
    void listen(SocketAddress address) throws IOException;

    T accept() throws IOException;

    <A> Future<T> accept(CompletionHandler<T, A> handler, A attachment);
}
