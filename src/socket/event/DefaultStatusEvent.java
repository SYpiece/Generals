package socket.event;

import socket.GameStatus;

public class DefaultStatusEvent extends EventBase implements StatusEvent {
    protected final GameStatus status;

    @Override
    public GameStatus getStatus() {
        return status;
    }

    public DefaultStatusEvent(GameStatus status) {
        super(EventType.StatusEvent);
        this.status = status;
    }
}
