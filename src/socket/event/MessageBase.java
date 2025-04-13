package socket.event;

import util.socket.Message;

public abstract class MessageBase implements Message {
    protected final EventType eventType;

    @Override
    public EventType getType() {
        return eventType;
    }

    protected MessageBase(EventType type) {
        eventType = type;
    }
}
