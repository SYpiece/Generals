package socket;

import socket.event.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DefaultSubServer 类实现了 Client 接口，用于处理与客户端的通信。
 * 它负责管理客户端连接、事件接收和发送、以及连接状态。
 */
public class DefaultSubServer implements Client {
    protected static final Logger logger = Logger.getLogger(DefaultSubServer.class.getName());

    protected final Socket socket;
    protected final ObjectOutputStream outputStream;
    protected final ObjectInputStream inputStream;
    protected volatile ClientState serverState = ClientState.Connected;

    /**
     * 获取当前连接的 Socket 对象。
     *
     * @return 当前连接的 Socket 对象
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * 获取当前客户端的状态。
     *
     * @return 当前客户端的状态
     */
    @Override
    public ClientState getClientState() {
        return serverState;
    }

    /**
     * 构造函数，初始化 DefaultSubServer 实例。
     *
     * @param socket 客户端连接的 Socket 对象
     * @throws IOException 如果初始化输入输出流时发生错误
     */
    public DefaultSubServer(Socket socket) throws IOException {
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void connect(SocketAddress socketAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A> Future<Void> connect(CompletionHandler<Void, A> handler, A attachment, SocketAddress socketAddress) {
        throw new UnsupportedOperationException();
    }

    final Thread handleThread = new Thread(this::doHandle);
    final Queue<Event> receivedEvents = new LinkedList<>();
    final Queue<HandlerAttachmentHolder<Void, ?, Event>> sendingHandlersHolders = new LinkedList<>();
    final Queue<HandlerAttachmentHolder<Event, ?, Void>> receivingHandlersHolders = new LinkedList<>();

    /**
     * 启动处理线程，开始处理客户端通信。
     *
     * @throws IOException 如果客户端状态不是 Connected
     */
    @Override
    public synchronized void handle() throws IOException {
        if (serverState != ClientState.Connected) {
            throw new IOException();
        }
        serverState = ClientState.Running;
        handleThread.start();
    }

    protected void doHandle() {
        try {
            while (serverState == ClientState.Running) {
                if (getSocket().getInputStream().available() > 0) {
                    Object object = null;
                    try {
                        object = inputStream.readUnshared();
                    } catch (IOException | ClassNotFoundException e) {
                        logger.log(Level.ALL, e.getMessage());
                    }
                    if (object instanceof Event) {
                        Event event = (Event) object;
                        HandlerAttachmentHolder<Event, ?, Void> holder = null;
                        synchronized (receivingHandlersHolders) {
                            holder = receivingHandlersHolders.poll();
                        }
                        if (holder != null) {
                            try {
                                holder.handlerCompleted(event);
                            } catch (Throwable e) {
                                logger.log(Level.ALL, e.getMessage());
                            }
                        } else {
                            receivedEvents.add(event);
                        }
                    }
                } else if (!receivedEvents.isEmpty()) {
                    HandlerAttachmentHolder<Event, ?, Void> receivingHolder = null;
                    synchronized (receivingHandlersHolders) {
                        receivingHolder = receivingHandlersHolders.poll();
                    }
                    if (receivingHolder != null) {
                        try {
                            receivingHolder.handlerCompleted(receivedEvents.poll());
                        } catch (Throwable e) {
                            logger.log(Level.ALL, e.getMessage());
                        }
                    }
                }

                HandlerAttachmentHolder<Void, ?, Event> sendingHolder = null;
                synchronized (sendingHandlersHolders) {
                    sendingHolder = sendingHandlersHolders.poll();
                }
                if (sendingHolder != null) {
                    Throwable throwable = null;
                    try {
                        outputStream.writeUnshared(sendingHolder.getTag());
                    } catch (IOException e) {
                        throwable = e;
                    }
                    try {
                        if (throwable == null) {
                            sendingHolder.handlerCompleted(null);
                        } else {
                            sendingHolder.handleFailed(throwable);
                        }
                    } catch (Throwable e) {
                        logger.log(Level.ALL, e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (!receivedEvents.isEmpty()) {
                synchronized (receivingHandlersHolders) {
                    while (!receivedEvents.isEmpty() && !receivingHandlersHolders.isEmpty()) {
                        try {
                            receivingHandlersHolders.poll().handlerCompleted(receivedEvents.poll());
                        } catch (Throwable e) {
                            logger.log(Level.ALL, e.getMessage());
                        }
                    }
                }
            }
            synchronized (receivingHandlersHolders) {
                for (HandlerAttachmentHolder<Event, ?, Void> holder : receivingHandlersHolders) {
                    try {
                        holder.handleFailed(new IOException());
                    } catch (Exception e) {
                        logger.log(Level.ALL, e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 获取当前接收队列中的事件数量。
     *
     * @return 接收队列中的事件数量
     * @throws IOException 如果客户端状态不是 Running
     */
    @Override
    public int available() throws IOException {
        if (serverState != ClientState.Running) {
            throw new IOException();
        }
        synchronized (receivedEvents) {
            return receivedEvents.size();
        }
    }

    /**
     * 从接收队列中获取一个事件。
     *
     * @return 接收的事件
     * @throws IOException 如果客户端状态不是 Running 或者获取事件时发生错误
     */
    @Override
    public Event receive() throws IOException {
        if (serverState != ClientState.Running) {
            throw new IOException();
        }
        try {
            return doReceive(null, null).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    /**
     * 异步接收事件，并使用指定的 CompletionHandler 处理接收结果。
     *
     * @param handler 处理接收结果的 CompletionHandler
     * @param attachment 附加对象
     * @param <A> 附加对象的类型
     * @return 表示异步操作的 Future 对象
     */
    @Override
    public <A> Future<Event> receive(CompletionHandler<Event, A> handler, A attachment) {
        return doReceive(handler, attachment);
    }

    protected <A> Future<Event> doReceive(CompletionHandler<Event, A> handler, A attachment) {
        final HandlerAttachmentHolder<Event, A, Void> holder = new HandlerAttachmentHolder<>();
        final SocketFuture<Event> receivingFuture = new SocketFuture<>(() -> {
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
        synchronized (receivingHandlersHolders) {
            if (serverState == ClientState.Running) {
                receivingHandlersHolders.add(holder);
            } else {
                new Thread(() -> {
                    try {
                        holder.handleFailed(new IOException());
                    } catch (Throwable e) {
                        logger.log(Level.ALL, e.getMessage());
                    }
                }).start();
            }
        }
        return receivingFuture;
    }

    /**
     * 发送一个事件到客户端。
     *
     * @param event 要发送的事件
     * @throws IOException 如果客户端状态不是 Running 或者发送事件时发生错误
     */
    @Override
    public void send(Event event) throws IOException {
        if (serverState != ClientState.Running) {
            throw new IOException();
        }
        try {
            doSend(null, null, event).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    /**
     * 异步发送事件，并使用指定的 CompletionHandler 处理发送结果。
     *
     * @param handler 处理发送结果的 CompletionHandler
     * @param attachment 附加对象
     * @param event 要发送的事件
     * @param <A> 附加对象的类型
     * @return 表示异步操作的 Future 对象
     */
    @Override
    public <A> Future<Void> send(CompletionHandler<Void, A> handler, A attachment, Event event) {
        return doSend(handler, attachment, event);
    }

    protected <A> Future<Void> doSend(CompletionHandler<Void, A> handler, A attachment, Event event) {
        final HandlerAttachmentHolder<Void, A, Event> holder = new HandlerAttachmentHolder<>();
        final SocketFuture<Void> sendingFuture = new SocketFuture<>(() -> {
            synchronized (sendingHandlersHolders) {
                sendingHandlersHolders.remove(holder);
            }
        });
        if (handler == null) {
            holder.setCompletionHandler(new CompletionHandler<Void, A>() {
                @Override
                public void completed(Void result, A attachment) {
                    sendingFuture.getCompletionHandler().completed(null, null);
                }

                @Override
                public void failed(Throwable exc, A attachment) {
                    sendingFuture.getCompletionHandler().failed(exc, null);
                }
            });
        } else {
            holder.setCompletionHandler(new CompletionHandler<Void, A>() {
                @Override
                public void completed(Void result, A attachment) {
                    holder.handlerCompleted(result);
                    sendingFuture.getCompletionHandler().completed(null, null);
                }

                @Override
                public void failed(Throwable exc, A attachment) {
                    holder.handleFailed(exc);
                    sendingFuture.getCompletionHandler().failed(exc, null);
                }
            });
        }
        holder.setAttachment(attachment);
        holder.setTag(event);
        synchronized (sendingHandlersHolders) {
            if (getClientState() == ClientState.Running) {
                sendingHandlersHolders.add(holder);
            } else {
                new Thread(() -> {
                    try {
                        holder.handleFailed(new IOException());
                    } catch (Throwable e) {
                        logger.log(Level.ALL, e.getMessage());
                    }
                }).start();
            }
        }
        return sendingFuture;
    }

    /**
     * 关闭客户端连接，释放资源。
     *
     * @throws IOException 如果关闭连接时发生错误
     */
    @Override
    public synchronized void close() throws IOException {
        if (serverState == ClientState.Closed) {
            throw new IOException();
        }
        serverState = ClientState.Closed;
        try {
            handleThread.join();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } finally {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                logger.log(Level.ALL, e.getMessage());
            }
        }
    }
}