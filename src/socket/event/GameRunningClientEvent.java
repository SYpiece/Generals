package socket.event;

import event.Event;
import model.Block;
import socket.GameClient;
import util.model.IdentityDocument;

import java.util.List;

public class GameRunningClientEvent extends Event {
    public static final int MAP_UPDATED = 68698;
    public static final int PLAYER_DEFEATED = 61269;
    protected final GameClient _client;
    protected final List<Block> _mapUpdates;
    protected final IdentityDocument _winnerID, _loserID;
    public GameClient getClient() {
        return _client;
    }
    public List<Block> getMapUpdates() {
        return _mapUpdates;
    }
    public IdentityDocument getWinnerID() {
        return _winnerID;
    }
    public IdentityDocument getLoserID() {
        return _loserID;
    }
    public GameRunningClientEvent(int eventType, GameClient client, List<Block> mapUpdates) {
        this(eventType, client, mapUpdates, null, null);
    }
    public GameRunningClientEvent(int eventType, GameClient client, IdentityDocument winnerID, IdentityDocument loserID) {
        this(eventType, client, null, winnerID, loserID);
    }
    protected GameRunningClientEvent(int eventType, GameClient client, List<Block> mapUpdates, IdentityDocument winnerID, IdentityDocument loserID) {
        super(eventType);
        _client = client;
        _mapUpdates = mapUpdates;
        _winnerID = winnerID;
        _loserID = loserID;
    }
}
