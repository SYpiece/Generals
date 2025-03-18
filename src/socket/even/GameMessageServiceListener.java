package socket.even;

import event.EventListener;

public interface GameMessageServiceListener extends EventListener<GameMessageServiceEvent> {
    void playerMessaged(GameMessageServiceEvent event);
}
