package socket;

import socket.event.Event;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class SocketFuture<T> implements Future<T> {
    protected volatile T result;
    protected final Object lock = new Object();
    protected final CompletionHandler<T, Void> completionHandler = new CompletionHandler<T, Void>() {
        @Override
        public void completed(T result, Void attachment) {
            done(result);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {}
    };
    protected final Runnable cancelHandler;
    protected volatile boolean isCancelled = false, isDone = false;

    protected CompletionHandler<T, Void> getCompletionHandler() {
        return completionHandler;
    }

    protected SocketFuture(Runnable cancelHandler) {
        this.cancelHandler = cancelHandler;
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
                cancelHandler.run();
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

    protected void done(T event) {
        synchronized (lock) {
            if (!isDone()) {
                result = event;
                isDone = true;
                lock.notifyAll();
            }
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait();
            }
            return result;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait(unit.toMillis(timeout));
            }
            return result;
        }
    }
}
