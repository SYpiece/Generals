package socket.event;

import java.io.Serializable;

public interface Event extends Serializable {
    EventType getType();
}
