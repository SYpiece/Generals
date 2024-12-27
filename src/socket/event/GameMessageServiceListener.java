package socket.event;

import event.EventListener;

public interface GameMessageServiceListener extends EventListener<GameMessageServiceEvent> {
    void playerMessaged(GameMessageServiceEvent event);
}
