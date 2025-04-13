package util.socket;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public abstract class ConnectionBase<T extends Message> implements Connection<T> {

    protected ConnectionBase() {}

    protected final Thread handlerThread = new Thread(this::doHandle, "handlerThread of " + this);
    protected final Queue<T> receivedValues = new LinkedList<>();
    final Queue<SocketFuture<?, Void, T>> sendingFutures = new LinkedList<>();
    final Queue<SocketFuture<?, T, Void>> receivingFutures = new LinkedList<>();

    public void handle() throws IOException {
        handlerThread.start();
    }

    private void doHandle() {
        SocketFuture<?, T, Void> receiving = null;
        while (!Thread.currentThread().isInterrupted()) {
            if (receiving == null) {
                synchronized (receivingFutures) {
                    receiving = receivingFutures.poll();
                }
            }

            T value = null;
            Throwable cause = null;
            try {
                if (doAvailable() > 0) {
                    value = doReceive();
                }
            } catch (Throwable e) {
                SocketLogger.logError(e);
                cause = e;
            }
            if (cause != null) {
                if (receiving != null) {
                    try {
                        receiving.failed(cause);
                    } catch (Throwable e) {
                        SocketLogger.logError(e);
                    }
                    receiving = null;
                }
            } else if (value == null) {
                if (receiving != null && !receivedValues.isEmpty()) {
                    try {
                        receiving.complete(receivedValues.poll());
                    } catch (Throwable e) {
                        SocketLogger.logError(e);
                    }
                    receiving = null;
                }
            } else {
                if (receiving == null) {
                    receivedValues.add(value);
                } else {
                    try {
                        receiving.complete(value);
                    } catch (Throwable e) {
                        SocketLogger.logError(e);
                    }
                    receiving = null;
                }
            }

            SocketFuture<?, Void, T> sending = null;
            synchronized (sendingFutures) {
                sending = sendingFutures.poll();
            }
            if (sending != null) {
                try {
                    doSend(sending.getSending());
                } catch (Throwable e) {
                    try {
                        sending.failed(e);
                    } catch (Throwable e2) {
                        SocketLogger.logError(e2);
                    }
                    SocketLogger.logError(e);
                }
                try {
                    sending.complete(null);
                } catch (Throwable e) {
                    SocketLogger.logError(e);
                }
            }
        }
    }

    protected abstract int doAvailable() throws IOException;

    protected abstract void doSend(T value) throws IOException;

    protected abstract T doReceive() throws IOException;

    @Override
    public int available() throws IOException {
        return receivedValues.size();
    }

    @Override
    public T receive() throws IOException {
        try {
            return doReceive(null, null).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<T> receive(A attachment, CompletionHandler<A, T> handler) throws IOException {
        return doReceive(attachment, handler);
    }

    private <A> Future<T> doReceive(A attachment, CompletionHandler<A, T> handler) throws IOException {
        if (handlerThread.isInterrupted()) {
            throw new IOException();
        }
        ReceivingFuture<A, T> future = new ReceivingFuture<>(attachment, handler, new CompletionHandler<Void, SocketFuture<A, T, Void>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, T, Void> result) {
                receivingFutures.remove(result);
            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        synchronized (receivingFutures) {
            receivingFutures.add(future);
        }
        return future;
    }

    @Override
    public void send(T message) throws IOException {
        try {
            doSend(null, null, message).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<Void> send(A attachment, CompletionHandler<A, Void> handler, T value) throws IOException {
        return doSend(attachment, handler, value);
    }

    private <A> Future<Void> doSend(A attachment, CompletionHandler<A, Void> handler, T value) throws IOException {
        if (handlerThread.isInterrupted()) {
            throw new IOException();
        }
        SendingFuture<A, T> future = new SendingFuture<>(attachment, handler, value, new CompletionHandler<Void, SocketFuture<A, Void, T>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, Void, T> result) {
                sendingFutures.remove(result);
            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        synchronized (sendingFutures) {
            sendingFutures.add(future);
        }
        return future;
    }

    @Override
    public void close() throws IOException {
        handlerThread.interrupt();
    }
}
