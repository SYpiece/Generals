package socket;

import util.socket.CompletionHandler;

class HandlerAttachmentHolder<V, A, T> {
    protected A attachment;
    protected CompletionHandler<V, A> handler;
    protected T tag;

    public CompletionHandler<V, A> getCompletionHandler() {
        return handler;
    }

    public void setCompletionHandler(CompletionHandler<V, A> handler) {
        this.handler = handler;
    }

    public A getAttachment() {
        return attachment;
    }

    public void setAttachment(A attachment) {
        this.attachment = attachment;
    }

    public T getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }

    public void handlerCompleted(V result) {
        getCompletionHandler().completed(result, attachment);
    }

    public void handleFailed(Throwable exc) {
        getCompletionHandler().failed(exc, attachment);
    }

    public HandlerAttachmentHolder() {
        this(null, null, null);
    }

    public HandlerAttachmentHolder(A attachment, CompletionHandler<V, A> handler, T tag) {
        this.handler = handler;
        this.attachment = attachment;
        this.tag = tag;
    }
}
