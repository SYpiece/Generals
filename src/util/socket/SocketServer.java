package util.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class SocketServer extends ServerBase<SocketAddress, Message> {
    protected ServerSocket serverSocket;

    public SocketServer() {}

    public SocketServer(SocketAddress address) throws IOException {
        listen(address);
    }

    @Override
    protected void doListen(SocketAddress address) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(address);
    }

    @Override
    protected SocketConnection doAccept() throws IOException {
        return new SocketConnection(serverSocket.accept());
    }

    @Override
    public void close() throws IOException {
        super.close();
        serverSocket.close();
    }
}
