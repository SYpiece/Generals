package util.socket;

import java.io.IOException;
import java.util.concurrent.Future;

public interface Listenable<L, T extends Message> {
    void listen(L address) throws IOException;

    Connection<T> accept() throws IOException;

    <A> Future<Connection<T>> accept(A attachment, CompletionHandler<A, Connection<T>> handler) throws IOException;
}
