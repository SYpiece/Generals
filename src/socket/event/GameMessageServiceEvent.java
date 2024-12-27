package socket.event;

import model.Message;
import socket.GameService;

public class GameMessageServiceEvent extends GameServiceEvent{
    public static final int PLAYER_MESSAGE = 91493;
    protected final Message _message;
    public Message getMessage() {
        return _message;
    }
    protected GameMessageServiceEvent(int eventType, GameService service, Message message) {
        super(eventType, service);
        _message = message;
    }
    public static GameMessageServiceEvent createPlayerMessageEvent(GameService service, Message message) {
        return new GameMessageServiceEvent(PLAYER_MESSAGE, service, message);
    }
}
