package util.socket;

import java.io.IOException;
import java.util.concurrent.Future;

public interface Writeable<T> {

    void send(T value) throws IOException;

    <A> Future<Void> send(A attachment, CompletionHandler<A, Void> handler, T value) throws IOException;
}
