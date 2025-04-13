package util.socket;

public interface CompletionHandler<A, T> {
    void completed(A attachment, T result);

    void failed(A attachment, Throwable e);
}
