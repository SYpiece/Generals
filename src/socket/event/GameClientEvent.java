package socket.event;

import event.Event;
import model.Game;
import socket.GameClient;
import socket.GameStatus;

public class GameClientEvent extends Event {
    public static final int GAME_SETTING_INITIALIZED = 63325;
    public static final int GAME_STATE_CHANGED = 60674;
    public static final int GAME_JOINED = 61040;
    public static final int GAME_EXITED = 69023;
    protected final GameClient _client;
    public GameClient getClient() {
        return _client;
    }
    protected GameClientEvent(int eventType, GameClient client) {
        super(eventType);
        _client = client;
    }
    public static GameClientEvent createJoinedGameEvent(GameClient client) {
        return new GameClientEvent(GAME_JOINED, client);
    }
    public static GameClientEvent createExitedGameEvent(GameClient client) {
        return new GameClientEvent(GAME_EXITED, client);
    }
    public static GameClientEvent createSettingInitialized(GameClient client) {
        return new GameClientEvent(GAME_SETTING_INITIALIZED, client);
    }
    public static GameClientEvent createStateChangedGameEvent(GameClient client) {
        return new GameClientEvent(GAME_STATE_CHANGED, client);
    }
}
