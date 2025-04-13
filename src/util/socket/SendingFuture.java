package util.socket;

class SendingFuture<A, S> extends SocketFuture<A, Void, S> {
    SendingFuture(A attachment, CompletionHandler<A, Void> handler, S message, CompletionHandler<Void, SocketFuture<A, Void, S>> cancelHandler) {
        super(attachment, handler, message, cancelHandler);
    }
}
