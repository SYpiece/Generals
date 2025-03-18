package socket.even;

import event.Event;
import model.BlockBase;
import socket.DefaultGameClient;
import util.model.IdentityDocument;

import java.util.List;

public class GameRunningClientEvent extends Event {
    public static final int MAP_UPDATED = 68698;
    public static final int PLAYER_DEFEATED = 61269;
    protected final DefaultGameClient _client;
    protected final List<BlockBase> _mapUpdates;
    protected final IdentityDocument _winnerID, _loserID;
    public DefaultGameClient getClient() {
        return _client;
    }
    public List<BlockBase> getMapUpdates() {
        return _mapUpdates;
    }
    public IdentityDocument getWinnerID() {
        return _winnerID;
    }
    public IdentityDocument getLoserID() {
        return _loserID;
    }
    public GameRunningClientEvent(int eventType, DefaultGameClient client, List<BlockBase> mapUpdates) {
        this(eventType, client, mapUpdates, null, null);
    }
    public GameRunningClientEvent(int eventType, DefaultGameClient client, IdentityDocument winnerID, IdentityDocument loserID) {
        this(eventType, client, null, winnerID, loserID);
    }
    protected GameRunningClientEvent(int eventType, DefaultGameClient client, List<BlockBase> mapUpdates, IdentityDocument winnerID, IdentityDocument loserID) {
        super(eventType);
        _client = client;
        _mapUpdates = mapUpdates;
        _winnerID = winnerID;
        _loserID = loserID;
    }
}
