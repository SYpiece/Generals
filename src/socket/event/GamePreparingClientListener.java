package socket.event;

import event.EventListener;

public interface GamePreparingClientListener extends EventListener<GamePreparingClientEvent> {
    void playerChangedForceState(GamePreparingClientEvent event);
    void playerTeamChanged(GamePreparingClientEvent event);
}
