package socket.event;

import model.Game;
import model.Message;
import socket.GameClient;

public class GameMessageClientEvent extends GameClientEvent {
    public static final int PLAYER_SEND_MESSAGE = 64504;
    public static final int PLAYER_JOINED = 61111;
    public static final int PLAYER_EXITED = 63725;
    public static final int PLAYER_TEAM_CHANGED = 68869;
    public static final int PLAYER_SURRENDERED = 62255;
    protected final Message _message;
    public Message getMessage() {
        return _message;
    }
    protected GameMessageClientEvent(int eventType, GameClient client, Message message) {
        super(eventType, client);
        _message = message;
    }
    public static GameMessageClientEvent createSendMessageEvent(GameClient client, Message message) {
        return new GameMessageClientEvent(PLAYER_SEND_MESSAGE, client, message);
    }
    public static GameMessageClientEvent createPlayerJoinedEvent(GameClient client, Message message) {
        return new GameMessageClientEvent(PLAYER_JOINED, client, message);
    }
    public static GameMessageClientEvent createPlayerExitedEvent(GameClient client, Message message) {
        return new GameMessageClientEvent(PLAYER_EXITED, client, message);
    }
    public static GameMessageClientEvent createTeamChangedEvent(GameClient client, Message message) {
        return new GameMessageClientEvent(PLAYER_TEAM_CHANGED, client, message);
    }
    public static GameMessageClientEvent createPlayerSurrenderedEvent(GameClient client, Message message) {
        return new GameMessageClientEvent(PLAYER_SURRENDERED, client, message);
    }
}
