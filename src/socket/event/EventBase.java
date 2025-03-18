package socket.event;

public abstract class EventBase implements Event {
    protected final EventType eventType;

    @Override
    public EventType getType() {
        return eventType;
    }

    protected EventBase(EventType type) {
        eventType = type;
    }
}
