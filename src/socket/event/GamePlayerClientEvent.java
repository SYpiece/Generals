package socket.event;

import model.Game;
import model.Player;
import socket.GameClient;
import socket.GameStatus;

public class GamePlayerClientEvent extends GameClientEvent {
    public static final int GAME_INITIALIZED = 62549;
    public static final int GAME_MAP_UPDATED = 64675;
    public static final int GAME_PLAYER_CHANGED = 67206;
    protected final Player _changedPlayer;
    public Player getChangedPlayer() {
        return _changedPlayer;
    }
    protected GamePlayerClientEvent(int eventType, GameClient client) {
        this(eventType, client, null);
    }
    protected GamePlayerClientEvent(int eventType, GameClient client, Player changedPlayer) {
        super(eventType, client);
        _changedPlayer = changedPlayer;
    }
    public static GamePlayerClientEvent createGameInitializedEvent(GameClient client) {
        return new GamePlayerClientEvent(GAME_INITIALIZED, client);
    }
    public static GamePlayerClientEvent createMapUpdatedEvent(GameClient client) {
        return new GamePlayerClientEvent(GAME_MAP_UPDATED, client);
    }
    public static GamePlayerClientEvent createPlayerChanged(GameClient client, Player changedPlayer) {
        return new GamePlayerClientEvent(GAME_PLAYER_CHANGED, client, changedPlayer);
    }
}
