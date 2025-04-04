package socket.even;

import event.EventListener;

public interface GameServiceListener extends EventListener<GameServiceEvent> {
    void playerJoined(GameServiceEvent event);
    void playerExited(GameServiceEvent event);
}
