package socket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Writeable<T> {

    void send(T event) throws IOException;

    <A> Future<Void> send(CompletionHandler<Void, A> handler, A attachment, T value);
}
