package socket.event;

import socket.GameStatus;
import util.socket.Message;

public interface StatusMessage extends Message {
    GameStatus getStatus();
}
