package socket.event;

import event.EventListener;

public interface GameClientListener extends EventListener<GameClientEvent> {
    void gameJoined(GameClientEvent event);
    void gameExited(GameClientEvent event);
    void gameSettingChanged(GameClientEvent event);
    void gameStatusChanged(GameClientEvent event);
}
