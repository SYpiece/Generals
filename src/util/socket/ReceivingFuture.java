package util.socket;

class ReceivingFuture<A, T> extends SocketFuture<A, T, Void> {
    ReceivingFuture(A attachment, CompletionHandler<A, T> handler, CompletionHandler<Void, SocketFuture<A, T, Void>> cancelHandler) {
        super(attachment, handler, null, cancelHandler);
    }
}
