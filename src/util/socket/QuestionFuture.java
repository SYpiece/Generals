package util.socket;

public class QuestionFuture<A, T> extends SocketFuture<A, T, T> {
    QuestionFuture(A attachment, CompletionHandler<A, T> handler, T sending, CompletionHandler<Void, SocketFuture<A, T, T>> cancelHandler) {
        super(attachment, handler, sending, cancelHandler);
    }
}
