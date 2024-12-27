package socket.event;

import event.EventListener;

public interface GameRunningClientListener extends EventListener<GameRunningClientEvent> {
    void mapUpdated(GameRunningClientEvent event);
    void playerDefeated(GameRunningClientEvent event);
}
