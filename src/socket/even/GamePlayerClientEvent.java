package socket.even;

import model.DefaultPlayer;
import socket.DefaultGameClient;

public class GamePlayerClientEvent extends GameClientEvent {
    public static final int GAME_INITIALIZED = 62549;
    public static final int GAME_MAP_UPDATED = 64675;
    public static final int GAME_PLAYER_CHANGED = 67206;
    protected final DefaultPlayer _changedPlayer;
    public DefaultPlayer getChangedPlayer() {
        return _changedPlayer;
    }
    protected GamePlayerClientEvent(int eventType, DefaultGameClient client) {
        this(eventType, client, null);
    }
    protected GamePlayerClientEvent(int eventType, DefaultGameClient client, DefaultPlayer changedPlayer) {
        super(eventType, client);
        _changedPlayer = changedPlayer;
    }
    public static GamePlayerClientEvent createGameInitializedEvent(DefaultGameClient client) {
        return new GamePlayerClientEvent(GAME_INITIALIZED, client);
    }
    public static GamePlayerClientEvent createMapUpdatedEvent(DefaultGameClient client) {
        return new GamePlayerClientEvent(GAME_MAP_UPDATED, client);
    }
    public static GamePlayerClientEvent createPlayerChanged(DefaultGameClient client, DefaultPlayer changedPlayer) {
        return new GamePlayerClientEvent(GAME_PLAYER_CHANGED, client, changedPlayer);
    }
}
