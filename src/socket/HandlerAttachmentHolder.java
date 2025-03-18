package socket;

import java.nio.channels.CompletionHandler;

class HandlerAttachmentHolder<V, A, T> {
    protected CompletionHandler<V, A> handler;
    protected A attachment;
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

    public HandlerAttachmentHolder(CompletionHandler<V, A> handler, A attachment, T tag) {
        this.handler = handler;
        this.attachment = attachment;
        this.tag = tag;
    }
}
