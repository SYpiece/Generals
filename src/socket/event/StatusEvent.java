package socket.event;

import socket.GameStatus;

public interface StatusEvent extends Event {
    GameStatus getStatus();
}
