package socket;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class SendingFuture implements Future<Void> {
    protected final Object lock = new Object();
    protected final CompletionHandler<Void, Void> completionHandler = new CompletionHandler<Void, Void>() {
        @Override
        public void completed(Void result, Void attachment) {
            done();
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            fail();
        }
    };
    protected final Runnable cancelHandler;
    protected volatile boolean isCancelled = false, isDone = false;

    protected CompletionHandler<Void, Void> getCompletionHandler() {
        return completionHandler;
    }

    public SendingFuture(Runnable cancelHandler) {
        this.cancelHandler = cancelHandler;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (isCancelled()) {
                return true;
            }
            if (isDone()) {
                return false;
            }
            if (mayInterruptIfRunning) {
                cancelHandler.run();
                isCancelled = true;
                isDone = true;
            }
            lock.notifyAll();
            return true;
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    protected void done() {
        synchronized (lock) {
            isDone = true;
        }
    }

    protected void fail() {
        synchronized (lock) {
            isDone = true;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait();
            }
            return null;
        }
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!isDone()) {
                lock.wait(unit.toMillis(timeout));
            }
            return null;
        }
    }
}
