package ui.event;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class JoinActionEvent {
    private SocketAddress roomSocketAddress;
    public JoinActionEvent(InetAddress roomInetAddress, int port) {
        roomSocketAddress = new InetSocketAddress(roomInetAddress, port);
    }
    public SocketAddress getRoomSocketAddress() {
        return roomSocketAddress;
    }
}
