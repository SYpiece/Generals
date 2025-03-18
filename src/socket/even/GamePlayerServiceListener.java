package socket.even;

import event.EventListener;

public interface GamePlayerServiceListener extends EventListener<GamePlayerServiceEvent> {
    void playerChangedForceState(GamePlayerServiceEvent event);
    void playerTeamChanged(GamePlayerServiceEvent event);
}
