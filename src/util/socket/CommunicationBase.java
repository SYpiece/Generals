package util.socket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class CommunicationBase<T extends Information> implements Communication<T> {

    protected CommunicationBase() {}

    protected final Thread handlerThread = new Thread(this::doHandle, "handlerThread of " + this);

    protected final Object listeningLock = new Object();
    protected final List<T>
            listenedQuestions = new ArrayList<>(),
            listeningQuestions = new LinkedList<>();

    protected final Queue<T> receivedValues = new LinkedList<>();
    final Queue<SocketFuture<?, Void, T>>
            sendingFutures = new LinkedList<>(),
            answeringFutures = new LinkedList<>();
    final Queue<SocketFuture<?, T, Void>>
            receivingFutures = new LinkedList<>(),
            listeningFutures =  new LinkedList<>();
    final Queue<SocketFuture<?, T, T>> queriedFutures = new LinkedList<>();

    @Override
    public List<T> getQuestions() {
        synchronized (listeningLock) {
            List<T> list = new LinkedList<>();
            list.addAll(listenedQuestions);
            list.addAll(listeningQuestions);
            return Collections.unmodifiableList(list);
        }
    }

    @Override
    public void handle() throws IOException {
        handlerThread.start();
    }

    private void doHandle() {
        SocketFuture<?, T, Void>
                receiving = null,
                listening = null;
        while (!Thread.currentThread().isInterrupted()) {
            if (receiving == null) {
                synchronized (receivingFutures) {
                    receiving = receivingFutures.poll();
                }
            }
            if (listening == null) {
                synchronized (listeningFutures) {
                    listening = listeningFutures.poll();
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
            if
//            if (value != null) {
//                try {
//                    SocketFuture<?, T, Void> future = null;
//                    if (value.isQuestion()) {
//                        synchronized (listeningFutures) {
//                            future = listeningFutures.poll();
//                        }
//                        if (future == null) {
//
//                        } else {
//
//                        }
//                    } else if (value.isAnswer()) {
//
//                    } else {
//                        synchronized (receivingFutures) {
//                            future = receivingFutures.poll();
//                        }
//                        if (future == null) {
//                            receivedValues.add(value);
//                        } else {
//                            future.complete(value);
//                        }
//                    }
//                } catch (Throwable e) {
//                    SocketLogger.logError(e);
//                }
//            }

            SocketFuture<?, ?, T> future = null;
            synchronized (sendingFutures) {
                future = sendingFutures.poll();
            }
            if (future != null) {
                try {
                    doSend(future.getSending());
                    future.complete(null);
                } catch (Throwable e) {
                    SocketLogger.logError(e);
                }
            }

            future = null;
            synchronized (answeringFutures) {
                future = answeringFutures.poll();
            }
            if (future != null) {
                try {
                    doSend(future.getSending());
                    future.complete(null);
                } catch (Throwable e) {
                    SocketLogger.logError(e);
                }
            }

            future = null;
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
    public T listen() throws IOException {
        try {
            return doListen(null, null).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<T> listen(A attachment, CompletionHandler<A, T> handler) throws IOException {
        return doListen(attachment, handler);
    }

    private <A> Future<T> doListen(A attachment, CompletionHandler<A, T> handler) throws IOException {
        if (handlerThread.isInterrupted()) {
            throw new IOException();
        }
        ListeningFuture<A, T> future = new ListeningFuture<>(attachment, handler, new CompletionHandler<Void, SocketFuture<A, T, Void>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, T, Void> result) {
                synchronized (queryingFutures) {
                    queryingFutures.remove(result);
                }
            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        synchronized (queryingFutures) {
            queryingFutures.add(future);
        }
        return future;
    }

    @Override
    public void answer(T question, T answer) throws IOException {
        try {
            doAnswer(null, question, null, answer).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<Void> answer(A attachment, T question, CompletionHandler<A, Void> handler, T answer) throws IOException {
        return doAnswer(attachment, question, handler, answer);
    }

    private <A> Future<Void> doAnswer(A attachment, T question, CompletionHandler<A, Void> handler, T answer) throws IOException {
        if (handlerThread.isInterrupted()) {
            throw new IOException();
        }
        if (!answer.isAnswer() || answer.getAnother() != question) {
            throw new IOException();
        }
        AnswerFuture<A, T> future = new AnswerFuture<>(attachment, handler, answer, new CompletionHandler<Void, SocketFuture<A, Void, T>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, Void, T> result) {
                synchronized (answeringFutures) {
                    answeringFutures.remove(result);
                }
            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        synchronized (answeringFutures) {
            answeringFutures.add(future);
        }
        return future;
    }

    @Override
    public T query(T question) throws IOException {
        try {
            return doQuery(null, null, question).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<T> query(A attachment, CompletionHandler<A, T> handler, T question) throws IOException {
        return doQuery(attachment, handler, question);
    }

    private <A> Future<T> doQuery(A attachment, CompletionHandler<A, T> handler, T question) throws IOException {
        if (handlerThread.isInterrupted()) {
            throw new IOException();
        }
        QuestionFuture<A, T> future = new QuestionFuture<>(attachment, handler, question, new CompletionHandler<Void, SocketFuture<A, T, T>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, T, T> result) {

            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        return future;
    }

    @Override
    public void close() throws IOException {
        handlerThread.interrupt();
    }
}
