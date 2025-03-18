package socket.even;

import event.Event;
import socket.DefaultGameClient;

public class GameClientEvent extends Event {
    public static final int GAME_SETTING_INITIALIZED = 63325;
    public static final int GAME_STATE_CHANGED = 60674;
    public static final int GAME_JOINED = 61040;
    public static final int GAME_EXITED = 69023;
    protected final DefaultGameClient _client;
    public DefaultGameClient getClient() {
        return _client;
    }
    protected GameClientEvent(int eventType, DefaultGameClient client) {
        super(eventType);
        _client = client;
    }
    public static GameClientEvent createJoinedGameEvent(DefaultGameClient client) {
        return new GameClientEvent(GAME_JOINED, client);
    }
    public static GameClientEvent createExitedGameEvent(DefaultGameClient client) {
        return new GameClientEvent(GAME_EXITED, client);
    }
    public static GameClientEvent createSettingInitialized(DefaultGameClient client) {
        return new GameClientEvent(GAME_SETTING_INITIALIZED, client);
    }
    public static GameClientEvent createStateChangedGameEvent(DefaultGameClient client) {
        return new GameClientEvent(GAME_STATE_CHANGED, client);
    }
}
