package socket.event;

import event.Event;
import model.Player;
import socket.GameService;

public class GameServiceEvent extends Event {
    public static final int PLAYER_JOINED = 35080;
    public static final int PLAYER_EXITED = 34973;
    protected final GameService _service;
    public GameService getService() {
        return _service;
    }
    protected GameServiceEvent(int eventType, GameService service) {
        super(eventType);
        _service = service;
    }
    public static GameServiceEvent createPlayerJoinedEvent(GameService service) {
        return new GameServiceEvent(PLAYER_JOINED, service);
    }
    public static GameServiceEvent createPlayerExitedEvent(GameService service) {
        return new GameServiceEvent(PLAYER_EXITED, service);
    }
}
