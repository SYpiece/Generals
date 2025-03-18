package socket;

import model.User;
import socket.event.Event;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public interface GameClient extends Client {
    User getUser();

    Socket getSocket();

    void connect(SocketAddress socketAddress) throws IOException;
}
