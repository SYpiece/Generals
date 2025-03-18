package socket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Readable<T> {

    int available() throws IOException;

    T receive() throws IOException;

    <A> Future<T> receive(CompletionHandler<T, A> handler, A attachment);
}
