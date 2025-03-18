package socket;

import java.nio.channels.CompletionHandler;

class ReceivingHandlerAttachmentHolder<V, A> {
    protected CompletionHandler<V, A> handler;
    protected A attachment;

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

    public void handlerCompleted(V result) {
        getCompletionHandler().completed(result, attachment);
    }

    public void handleFailed(Throwable exc) {
        getCompletionHandler().failed(exc, attachment);
    }

    public ReceivingHandlerAttachmentHolder() {
        this(null, null);
    }

    public ReceivingHandlerAttachmentHolder(CompletionHandler<V, A> handler, A attachment) {
        this.handler = handler;
        this.attachment = attachment;
    }
}
