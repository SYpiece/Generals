package util.socket;

public class AcceptationFuture<A, T> extends SocketFuture<A, Connection<T>, Void> {
    AcceptationFuture(A attachment, CompletionHandler<A, Connection<T>> handler, CompletionHandler<Void, SocketFuture<A, Connection<T>, Void>> cancelHandler) {
        super(attachment, handler, null, cancelHandler);
    }
}
