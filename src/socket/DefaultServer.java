package socket;

import socket.event.Event;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.*;
import java.util.concurrent.*;

public class DefaultServer implements Server {
    protected volatile ServerState serverState;
    protected ServerSocket serverSocket;
    protected final List<DefaultSubServer> subServers = new ArrayList<>();

    @Override
    public ServerState getServerState() {
        return serverState;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public List<DefaultSubServer> getSubServers() {
        return Collections.unmodifiableList(new ArrayList<>(subServers));
    }

    public DefaultServer() {
        serverState = ServerState.Preparing;
    }

    public DefaultServer(SocketAddress address) throws IOException {
        this();
        listen(address);
    }

    final Thread listenThread = new Thread(this::doListen);
    final Queue<Socket> acceptingSockets = new LinkedList<>();
    final Queue<HandlerAttachmentHolder<DefaultSubServer, ?, Void>> acceptingHandlersHolders = new LinkedList<>();

    @Override
    public synchronized void listen(SocketAddress socketAddress) throws IOException {
        if (serverState != ServerState.Preparing) {
            throw new IOException();
        }
        serverSocket = new ServerSocket();
        serverSocket.bind(socketAddress);
        serverState = ServerState.Listened;
        listenThread.start();
    }

    protected void doListen() {
        while (serverState == ServerState.Listened || serverState == ServerState.Running) {
            try {
                final Socket socket = serverSocket.accept();
                HandlerAttachmentHolder<DefaultSubServer, ?, Void> holder = null;
                synchronized (acceptingHandlersHolders) {
                    holder = acceptingHandlersHolders.poll();
                }
                if (holder != null) {
                    try {
                        holder.handlerCompleted(new DefaultSubServer(socket));
                    } catch (Throwable e) {

                    }
                } else {
                    synchronized (acceptingSockets) {
                        acceptingSockets.add(socket);
                    }
                }
            } catch (IOException e) {

            }
        }
    }

    @Override
    public DefaultSubServer accept() throws IOException {
        if (serverState != ServerState.Listened && serverState != ServerState.Running) {
            throw new IOException();
        }
        try {
            return doAccept(null, null).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<DefaultSubServer> accept(CompletionHandler<DefaultSubServer, A> handler, A attachment) {
        return doAccept(handler, attachment);
    }

    protected <A> Future<DefaultSubServer> doAccept(CompletionHandler<DefaultSubServer, A> handler, A attachment) {
        final HandlerAttachmentHolder<DefaultSubServer, A, Void> holder = new HandlerAttachmentHolder<>();
        final SocketFuture<DefaultSubServer> acceptingFuture = new SocketFuture<>(() -> {
            synchronized (receivingHandlersHolders) {
                receivingHandlersHolders.remove(holder);
            }
        });
    }

    protected final Thread handleThread = new Thread(this::doHandle);
    protected final Queue<Event> receivedEvents = new LinkedList<>();
    final Queue<HandlerAttachmentHolder<Void, ?, Event>> sendingHandlerHolders = new LinkedList<>();
    final Queue<ReceivingHandlerAttachmentHolder<Event, ?>> receivingHandlersHolders = new LinkedList<>();

    @Override
    public synchronized void handle() throws IOException {
        if (serverState != ServerState.Listened) {
            throw new IOException();
        }
        handleThread.start();
    }

    protected void doHandle() {
        try {
            while (serverState == ServerState.Running) {
                Event event = null;
                synchronized (sendingEvents) {
                    if (!sendingEvents.isEmpty()) {
                        event = sendingEvents.poll();
                    }
                }
                if (event != null) {
                    synchronized (subServers) {
                        for (DefaultGameSubServer subServer : subServers) {
                            subServer.send(event);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int available() throws IOException {
        if (serverState != ServerState.Running) {
            throw new IOException();
        }
        synchronized (receivedEvents) {
            return receivedEvents.size();
        }
    }

    @Override
    public Event receive() throws IOException {
        if (getServerState() != ServerState.Running) {
            throw new IOException();
        }
        try {
            return doReceive(null, null).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <A> Future<Event> receive(CompletionHandler<Event, A> handler, A attachment) {
        return doReceive(handler, attachment);
    }

    protected <A> Future<Event> doReceive(CompletionHandler<Event, A> handler, A attachment) {
        final ReceivingHandlerAttachmentHolder<Event, A> holder = new ReceivingHandlerAttachmentHolder<>();
        final ReceivingFuture receivingFuture = new ReceivingFuture(() -> {
            synchronized (receivingHandlersHolders) {
                receivingHandlersHolders.remove(holder);
            }
        });
        if (handler == null) {
            holder.setCompletionHandler(new CompletionHandler<Event, A>() {
                @Override
                public void completed(Event result, A attachment) {
                    receivingFuture.getCompletionHandler().completed(result, null);
                }

                @Override
                public void failed(Throwable exc, A attachment) {
                    receivingFuture.getCompletionHandler().failed(exc, null);
                }
            });
        } else {
            holder.setCompletionHandler(new CompletionHandler<Event, A>() {
                @Override
                public void completed(Event result, A attachment) {
                    handler.completed(result, attachment);
                    receivingFuture.getCompletionHandler().completed(result, null);
                }

                @Override
                public void failed(Throwable exc, A attachment) {
                    handler.failed(exc, attachment);
                    receivingFuture.getCompletionHandler().failed(exc, null);
                }
            });
            holder.setAttachment(attachment);
        }
        synchronized (receivedEvents) {
            receivingHandlersHolders.add(holder);
        }
        return receivingFuture;
    }

    @Override
    public void send(Event event) throws IOException {
        if (getServerState() != ServerState.Running) {
            throw new IOException();
        }
        synchronized (sendingEvents) {
            sendingEvents.add(event);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        if (serverState == ServerState.Closed) {
            throw new IOException();
        }
        if (serverState == ServerState.Running) {
            serverSocket.close();
        }
        serverState = ServerState.Closed;
    }
}