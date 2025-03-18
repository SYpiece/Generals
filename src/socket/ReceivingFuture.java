package socket;

import socket.event.Event;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ReceivingFuture implements Future<Event> {
    protected volatile Event result;
    protected final Object lock = new Object();
    protected final CompletionHandler<Event, Void> completionHandler = new CompletionHandler<Event, Void>() {
        @Override
        public void completed(Event result, Void attachment) {
            done(result);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {}
    };
    protected final Runnable cancelHandler;
    protected volatile boolean isCancelled = false, isDone = false;

    protected CompletionHandler<Event, Void> getCompletionHandler() {
        return completionHandler;
    }

    protected ReceivingFuture(Runnable cancelHandler) {
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

    protected void done(Event event) {
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
    public Event get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait();
            }
            return result;
        }
    }

    @Override
    public Event get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait(unit.toMillis(timeout));
            }
            return result;
        }
    }
}
