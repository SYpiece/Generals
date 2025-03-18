package socket.even;

import event.EventListener;

public interface GameMessageClientListener extends EventListener<GameMessageClientEvent> {
    void playerSentMessage(GameMessageClientEvent event);
    void playerJoined(GameMessageClientEvent event);
    void playerExited(GameMessageClientEvent event);
    void playerTeamChanged(GameMessageClientEvent event);
    void playerSurrendered(GameMessageClientEvent event);
}
