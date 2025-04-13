package util.socket;

import java.io.IOException;
import java.util.concurrent.Future;

public interface Questionable<T extends Information> {
    T query(T question) throws IOException;

    <A> Future<T> query(A attachment, CompletionHandler<A, T> handler, T question) throws IOException;
}
