package socket.event;

import event.EventListener;

public interface GamePlayerClientListener extends EventListener<GamePlayerClientEvent> {
    void gamePlayerChanged(GamePlayerClientEvent event);
    void gameInitialized(GamePlayerClientEvent event);
    void gameMapUpdated(GamePlayerClientEvent event);
}
