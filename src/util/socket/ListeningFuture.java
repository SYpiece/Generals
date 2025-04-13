package util.socket;

public class ListeningFuture<A, T> extends SocketFuture<A, T, Void> {
    ListeningFuture(A attachment, CompletionHandler<A, T> handler, CompletionHandler<Void, SocketFuture<A, T, Void>> cancelHandler) {
        super(attachment, handler, null, cancelHandler);
    }
}
