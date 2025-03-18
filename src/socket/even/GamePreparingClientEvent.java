package socket.even;

import event.Event;
import model.DefaultPlayer;
import socket.DefaultGameClient;

import java.util.List;

public class GamePreparingClientEvent extends Event {
    public static final int PLAYER_CHANGED_FORCE_STATE = 64980;
    public static final int PLAYER_TEAM_CHANGED = 60719;
    protected final DefaultGameClient _client;
    protected final List<DefaultPlayer.GamePlayer> _players;
    public DefaultGameClient getClient() {
        return _client;
    }
    public List<DefaultPlayer.GamePlayer> getPlayers() {
        return _players;
    }
    public GamePreparingClientEvent(int eventType, DefaultGameClient client, List<DefaultPlayer.GamePlayer> players) {
        super(eventType);
        _client = client;
        _players = players;
    }
}
