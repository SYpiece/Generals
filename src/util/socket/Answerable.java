package util.socket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

public interface Answerable<T extends Information> {
    List<T> getQuestions() throws IOException;

    T listen() throws IOException;

    <A> Future<T> listen(A attachment, CompletionHandler<A, T> handler) throws IOException;

    void answer(T question, T answer) throws IOException;

    <A> Future<Void> answer(A attachment, T question, CompletionHandler<A, Void> handler, T answer) throws IOException;
}
