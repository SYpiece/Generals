package socket.event;

import socket.GameStatus;

public class DefaultStatusMessage extends MessageBase implements StatusMessage {
    protected final GameStatus status;

    @Override
    public GameStatus getStatus() {
        return status;
    }

    public DefaultStatusMessage(GameStatus status) {
        super(EventType.StatusEvent);
        this.status = status;
    }
}
