package util.socket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public abstract class ServerBase<L, T> implements Server<L, T> {
    protected volatile ServerState serverState = ServerState.Preparing;
    protected final List<Connection<T>> connections = new ArrayList<>();

    @Override
    public ServerState getServerState() {
        return serverState;
    }

    @Override
    public List<Connection<T>> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    protected ServerBase() {}

    final Thread
            listenThread = new Thread(this::doListen, "listenThread of " + this),
            acceptingThread = new Thread(this::doAccepting, "acceptingThread of " + this);
    final Queue<Throwable> errors = new LinkedList<>();
    final Queue<Connection<T>> acceptingConnections = new LinkedList<>();
    final Queue<SocketFuture<?, Connection<T>, Void>> acceptationFutures = new LinkedList<>();

    @Override
    public void listen(L address) throws IOException {
        if (serverState != ServerState.Preparing) {
            throw new IOException();
        }
        doListen(address);
        serverState = ServerState.Listened;
        listenThread.start();
    }

    protected abstract void doListen(L address) throws IOException;

    private void doListen() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final Connection<T> connection = doAccept();
                synchronized (acceptingConnections) {
                    acceptingConnections.add(connection);
                }
            } catch (Throwable e) {
                synchronized (errors) {
                    errors.add(e);
                }
            }
        }
    }

    private void doAccepting() {
        SocketFuture<?, Connection<T>, Void> future = null;
        while (!Thread.currentThread().isInterrupted()) {
            if (future == null) {
                synchronized (acceptationFutures) {
                    future = acceptationFutures.poll();
                }
            } else {
                Throwable error = null;
                synchronized (errors) {
                    error = errors.poll();
                }
                if (error != null) {
                    try {
                        future.failed(error);
                    } catch (Throwable e) {
                        SocketLogger.logError(e);
                    }
                    continue;
                }

                Connection<T> connection = null;
                synchronized (acceptingConnections) {
                    connection = acceptingConnections.poll();
                }
                if (connection != null) {
                    try {
                        future.complete(connection);
                    } catch (Throwable e) {
                        SocketLogger.logError(e);
                    }
                }
            }
        }
    }

    protected abstract Connection<T> doAccept() throws IOException;

    @Override
    public Connection<T> accept() throws IOException {
        try {
            return doAccept(null, null).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<Connection<T>> accept(A attachment, CompletionHandler<A, Connection<T>> handler) throws IOException {
        return doAccept(attachment, handler);
    }

    private  <A> Future<Connection<T>> doAccept(A attachment, CompletionHandler<A, Connection<T>> handler) throws IOException {
        if (serverState != ServerState.Listened && serverState != ServerState.Running) {
            throw new IOException();
        }
        AcceptationFuture<A, T> acceptationFuture = new AcceptationFuture<>(attachment, handler, new CompletionHandler<Void, SocketFuture<A, Connection<T>, Void>>() {
            @Override
            public void completed(Void attachment, SocketFuture<A, Connection<T>, Void> result) {
                synchronized (acceptationFutures) {
                    acceptationFutures.remove(result);
                }
            }
            @Override
            public void failed(Void attachment, Throwable e) {}
        });
        synchronized (acceptationFutures) {
            acceptationFutures.add(acceptationFuture);
        }
        return acceptationFuture;
    }
    @Override
    public synchronized void close() throws IOException {
        if (serverState == ServerState.Closed) {
            throw new IOException();
        }
        listenThread.interrupt();
        acceptingThread.interrupt();
        serverState = ServerState.Closed;
    }
}