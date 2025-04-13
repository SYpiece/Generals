package util.socket;

import java.io.IOException;
import java.util.concurrent.Future;

public interface Readable<T extends Message> {

    int available() throws IOException;

    T receive() throws IOException;

    <A> Future<T> receive(A attachment, CompletionHandler<A, T> handler) throws IOException;
}
