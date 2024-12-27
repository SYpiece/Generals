package socket.event;

import event.Event;
import model.Player;
import socket.GameClient;

import java.util.List;

public class GamePreparingClientEvent extends Event {
    public static final int PLAYER_CHANGED_FORCE_STATE = 64980;
    public static final int PLAYER_TEAM_CHANGED = 60719;
    protected final GameClient _client;
    protected final List<Player.GamePlayer> _players;
    public GameClient getClient() {
        return _client;
    }
    public List<Player.GamePlayer> getPlayers() {
        return _players;
    }
    public GamePreparingClientEvent(int eventType, GameClient client, List<Player.GamePlayer> players) {
        super(eventType);
        _client = client;
        _players = players;
    }
}
