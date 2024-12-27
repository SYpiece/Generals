package event;

public abstract class Event {
    protected final int _eventType;
    protected Event(int eventType) {
        _eventType = eventType;
    }
    public int getEventType() {
        return _eventType;
    }
}
