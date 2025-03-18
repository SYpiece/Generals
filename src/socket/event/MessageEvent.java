package socket.event;

import model.Message;

public interface MessageEvent extends Event {
    Message getMessage();
}
