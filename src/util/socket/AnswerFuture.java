package util.socket;

public class AnswerFuture<A, T> extends SocketFuture<A, Void, T> {
    AnswerFuture(A attachment, CompletionHandler<A, Void> handler, T sending, CompletionHandler<Void, SocketFuture<A, Void, T>> cancelHandler) {
        super(attachment, handler, sending, cancelHandler);
    }
}
