package socket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import event.SyncEventListenerList;
import model.*;
import resource.VersionResource;
import resource.color.Color;
import resource.version.Version;
import socket.event.*;

public class GameService implements Closeable {
    //System
    public static final int REQUEST_PLAYER_INFORMATION = 1228;
    public static final int GAME_SET_PLAYER_ID = 463;//IdentityDocument
    //Global
    public static final int GAME_STATE_CHANGED = 5881;//GameState
    public static final int PLAYER_SEND_MESSAGE = 9979;//Message
    public static final int GAME_SETTING_INITIALIZED = 4309;//GameSetting
    public static final int GAME_PLAYER_JOINED = 2816;//GamePlayer
    public static final int GAME_PLAYER_EXITED = 3833;//GamePlayer
    //Preparing
    public static final int GAME_PLAYER_FORCE_CHANGED = 7996;//GamePlayer
    public static final int GAME_PLAYER_TEAM_CHANGED = 488;//GamePlayer
    //Initializing
    public static final int GAME_MODEL_INITIALIZED = 527;//Game
    //Running
    public static final int GAME_MAP_UPDATED = 5130;//List<Block>
    public static final int REQUEST_PLAYER_ORDER_ACTION = 2985;

    protected final Socket _socket;
    protected ObjectInputStream _inputStream;
    protected ObjectOutputStream _outputStream;
    GameService(Socket socket) throws IOException {
        _socket = socket;
        _outputStream = new ObjectOutputStream(_socket.getOutputStream());
        _outputStream.writeObject(VersionResource.getVersion());
        _inputStream = new ObjectInputStream(_socket.getInputStream());
        Version version;
        try {
            version = (Version) _inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SocketException("version error");
        }
        if (!version.equals(VersionResource.getVersion())) {
            throw new SocketException("version mismatch");
        }
    }
    public void start() {
        _serviceThread.start();
    }
    private volatile Player.GamePlayer _player;
    public Player.GamePlayer getPlayer() {
        return _player;
    }
    private volatile GameStatus _gameStatus;
    public GameStatus getGameState() {
        return _gameStatus;
    }
    public void setGameStatus(GameStatus state) {
        if (_gameStatus != state) {
            _gameStatus = state;
            writeGameMessage(new GameMessage(GameService.GAME_STATE_CHANGED, state));
        }
    }
    private volatile Order _order;
    private final LinkedList<GameMessage> _writingList = new LinkedList<>();
    private void serviceThreadHandle() throws IOException, ClassNotFoundException {
//        System.out.println("serviceThreadHandle");
        InputStream input = _socket.getInputStream();
        while (true) {
            if (input.available() > 0) {
                GameMessage gameMessage = (GameMessage) _inputStream.readObject();
//                System.out.println("service:" + gameMessage);
                switch (gameMessage.getMessageType()) {
                    //System
                    case GameClient.RESPOND_INFORMATION_REQUEST: {
                        Player.PlayerInformation information = gameMessage.getData();
                        _player = Player.GamePlayer.createCompetitor(information, Color.RED, 1);
                        _gameServiceListenerList.fireListenerEvent(GameServiceEvent.createPlayerJoinedEvent(this));
                        writeGameMessage(new GameMessage(GameService.GAME_SET_PLAYER_ID, _player.getID()));
                        break;
                    }
                    //Global
                    case GameClient.PLAYER_JOINED:
                        writeGameMessage(new GameMessage(GameService.REQUEST_PLAYER_INFORMATION));
                        break;
                    case GameClient.PLAYER_EXITED:
                        _gameServiceListenerList.fireListenerEvent(GameServiceEvent.createPlayerExitedEvent(this));
                        return;
                    case GameClient.PLAYER_SEND_MESSAGE: {
                        Message message = gameMessage.getData();
                        _gameMessageServiceListenerList.fireListenerEvent(GameMessageServiceEvent.createPlayerMessageEvent(this, message));
                        break;
                    }
                    //Preparing
                    case GameClient.PLAYER_SET_FORCE_STATE:
                        _player.setForceStart(gameMessage.getData());
                        _gamePlayerServiceListenerList.fireListenerEvent(GamePlayerServiceEvent.createForceStateEvent(this));
                        break;
                    case GameClient.PLAYER_SET_TEAM:
                        _player.setTeam(gameMessage.getData());
                        _gamePlayerServiceListenerList.fireListenerEvent(GamePlayerServiceEvent.createTeamChangedEvent(this));
                        break;
                    //Running
                    case GameClient.PLAYER_ORDER_ACTION:
                        _order = gameMessage.getData();
                        break;
                    default:
                        break;
                }
            }
            synchronized (_writingList) {
                if (!_writingList.isEmpty()) {
                    _outputStream.writeObject(_writingList.poll());
                }
            }
            Thread.yield();
        }
    }
    public Order requestOrder() {
        synchronized (_writingList) {
            _writingList.push(new GameMessage(GameService.REQUEST_PLAYER_ORDER_ACTION));
        }
        while (_order == null) {
            Thread.yield();
        }
        Order order = _order;
        _order = null;
        return _order;
    }
    public void writeMapUpdates(List<Block> models) {
        writeGameMessage(new GameMessage(GameService.GAME_MAP_UPDATED, models));
    }
    public void writeGameSetting(Game.GameSetting setting) {
        writeGameMessage(new GameMessage(GameService.GAME_SETTING_INITIALIZED, setting));
    }
    public void writePlayerMessage(Message message) {
        writeGameMessage(new GameMessage(GameService.PLAYER_SEND_MESSAGE, message));
    }
    public void writeGameModel(Game game) {
        writeGameMessage(new GameMessage(GameService.GAME_MODEL_INITIALIZED, game));
    }
    public void writeJoinedPlayer(Player.GamePlayer player) {
        writeGameMessage(new GameMessage(GameService.GAME_PLAYER_JOINED, player));
    }
    public void writeExitedPlayer(Player.GamePlayer player) {
        writeGameMessage(new GameMessage(GameService.GAME_PLAYER_EXITED, player));
    }
    public void writeForceChangedPlayer(Player.GamePlayer player) {
        writeGameMessage(new GameMessage(GameService.GAME_PLAYER_FORCE_CHANGED, player));
    }
    public void writeTeamChangedPlayer(Player.GamePlayer player) {
        writeGameMessage(new GameMessage(GameService.GAME_PLAYER_TEAM_CHANGED, player));
    }
    private void writeGameMessage(GameMessage message) {
        synchronized (_writingList) {
            _writingList.push(message);
        }
    }
    @Override
    public void close() throws IOException {
        _serviceThread.interrupt();
        try {
            _serviceThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        _socket.close();
    }
    protected final GameServiceListenerList _gameServiceListenerList = new GameServiceListenerList();
    public void addServiceListener(GameServiceListener listener) {
        _gameServiceListenerList.addListener(listener);
    }
    public void removeServiceListener(GameServiceListener listener) {
        _gameServiceListenerList.removeListener(listener);
    }
    private final Thread _serviceThread = new Thread(() -> {
        try {
            serviceThreadHandle();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    });
    protected static final class GameServiceListenerList extends SyncEventListenerList<GameServiceListener, GameServiceEvent> {
        public GameServiceListenerList() {
            super(GameServiceListener.class, GameServiceEvent.class);
        }
        @Override
        public synchronized void fireListenerEvent(GameServiceEvent event) {
            switch (event.getEventType()) {
                case GameServiceEvent.PLAYER_JOINED:
                    for (GameServiceListener listener : _listenerList) {
                        listener.playerJoined(event);
                    }
                    break;
                case GameServiceEvent.PLAYER_EXITED:
                    for (GameServiceListener listener : _listenerList) {
                        listener.playerExited(event);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    protected final GamePlayerServiceListenerList _gamePlayerServiceListenerList = new GamePlayerServiceListenerList();
    public void addPlayerServiceListener(GamePlayerServiceListener listener) {
        _gamePlayerServiceListenerList.addListener(listener);
    }
    public void removePlayerServiceListener(GamePlayerServiceListener listener) {
        _gamePlayerServiceListenerList.removeListener(listener);
    }
    protected static final class GamePlayerServiceListenerList extends SyncEventListenerList<GamePlayerServiceListener, GamePlayerServiceEvent> {
        public GamePlayerServiceListenerList() {
            super(GamePlayerServiceListener.class, GamePlayerServiceEvent.class);
        }
        @Override
        public synchronized void fireListenerEvent(GamePlayerServiceEvent event) {
            switch (event.getEventType()) {
                case GamePlayerServiceEvent.PLAYER_CHANGED_FORCE_STATE:
                    for (GamePlayerServiceListener listener : _listenerList) {
                        listener.playerChangedForceState(event);
                    }
                    break;
                case GamePlayerServiceEvent.PLAYER_TEAM_CHANGED:
                    for (GamePlayerServiceListener listener : _listenerList) {
                        listener.playerTeamChanged(event);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    protected final GameMessageServiceListenerList _gameMessageServiceListenerList = new GameMessageServiceListenerList();
    public void addMessageServiceListener(GameMessageServiceListener listener) {
        _gameMessageServiceListenerList.addListener(listener);
    }
    public void removeMessageServiceListener(GameMessageServiceListener listener) {
        _gameMessageServiceListenerList.removeListener(listener);
    }
    protected static final class GameMessageServiceListenerList extends SyncEventListenerList<GameMessageServiceListener, GameMessageServiceEvent> {
        public GameMessageServiceListenerList() {
            super(GameMessageServiceListener.class, GameMessageServiceEvent.class);
        }
        @Override
        public synchronized void fireListenerEvent(GameMessageServiceEvent event) {
            if (event.getEventType() == GameMessageServiceEvent.PLAYER_MESSAGE) {
                for (GameMessageServiceListener listener : _listenerList) {
                    listener.playerMessaged(event);
                }
            }
        }
    }
}
