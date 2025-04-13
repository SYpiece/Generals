package util.socket;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

abstract class SocketFuture<A, R, S> implements Future<R> {

    protected final Object lock = new Object();
    protected final CompletionHandler<Void, SocketFuture<A, R, S>> cancelHandler;
    protected volatile boolean isCancelled = false, isDone = false;

    protected final A attachment;
    protected final CompletionHandler<A, R> handler;
    protected volatile R receiving;
    protected final S sending;
    protected Throwable error;

    A getAttachment() {
        return attachment;
    }

    CompletionHandler<A, R> getHandler() {
        return handler;
    }

    S getSending() {
        return sending;
    }

    Throwable getError() {
        return error;
    }

    SocketFuture(A attachment, CompletionHandler<A, R> handler, S sending, CompletionHandler<Void, SocketFuture<A, R, S>> cancelHandler) {
        this.attachment = attachment;
        this.handler = handler;
        this.sending = sending;
        this.cancelHandler = Objects.requireNonNull(cancelHandler);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (isDone()) {
                return false;
            }
            if (isCancelled()) {
                return true;
            }
            if (mayInterruptIfRunning) {
                cancelHandler.completed(null, this);
                isCancelled = true;
            }
            lock.notifyAll();
            return true;
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    void complete(R receiving) {
        synchronized (lock) {
            if (!isDone()) {
                this.receiving = receiving;
                isDone = true;
                lock.notifyAll();
                if (handler != null) {
                    handler.completed(attachment, receiving);
                }
            }
        }
    }

    void failed(Throwable e) {
        synchronized (lock) {
            if (!isDone()) {
                this.error = e;
                isDone = true;
                lock.notifyAll();
                if (handler != null) {
                    handler.failed(attachment, e);
                }
            }
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait();
            }
            if (getError() != null) {
                throw new ExecutionException(getError());
            }
            return receiving;
        }
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait(unit.toMillis(timeout));
            }
            if (getError() != null) {
                throw new ExecutionException(getError());
            }
            return receiving;
        }
    }
}
