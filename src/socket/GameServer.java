package socket;

import model.Game;
import socket.event.Event;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface GameServer extends Server {
    Game getGame();

    ServerSocket getServerSocket();

    List<? extends GameSubServer> getSubServers();

    interface GameSubServer extends Client {
        Socket getSocket();

        @Override
        default void connect(SocketAddress socketAddress) {
            throw new UnsupportedOperationException();
        }

        @Override
        default Future<Void> connect(CompletionHandler<Void, ?> handler, Object attachment, SocketAddress socketAddress) {
            throw new UnsupportedOperationException();
        }
    }
}
