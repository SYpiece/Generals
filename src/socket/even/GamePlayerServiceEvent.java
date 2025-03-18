package socket.even;

import socket.GameService;

public class GamePlayerServiceEvent extends GameServiceEvent {
    public static final int PLAYER_CHANGED_FORCE_STATE = 15289;
    public static final int PLAYER_TEAM_CHANGED = 10399;
    protected GamePlayerServiceEvent(int eventType, GameService service) {
        super(eventType, service);
    }
    public static GamePlayerServiceEvent createForceStateEvent(GameService service) {
        return new GamePlayerServiceEvent(PLAYER_CHANGED_FORCE_STATE, service);
    }
    public static GamePlayerServiceEvent createTeamChangedEvent(GameService service) {
        return new GamePlayerServiceEvent(PLAYER_TEAM_CHANGED, service);
    }
}
