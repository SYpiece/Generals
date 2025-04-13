package socket;

import model.User;
import util.socket.Message;
import socket.event.StatusMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>这是一个是客户端用于进行游戏的类，该类提供了许多与服务端通讯和处理任务的功能和方法</p>
 */
public class DefaultGameClient implements GameClient {
    protected Socket socket;
    protected final User user;
    protected final Thread thread = new Thread(this::socketHandle);
    protected volatile boolean isRunning = true;
    protected final Queue<Object> queue = new LinkedBlockingQueue<>();

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    public DefaultGameClient(User user) {
        this.user = user;
        socket = new Socket();
    }

    public DefaultGameClient(User user, InetAddress address, int port) throws IOException {
        this(user);
        connect(address, port);
    }

    public DefaultGameClient(User user, SocketAddress socketAddress) throws IOException {
        this(user);
        connect(socketAddress);
    }

    public void connect(SocketAddress socketAddress) throws IOException {
        throw new Suppor
    }

    protected void socketHandle() {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ) {
            while (isRunning) {
                if (inputStream.available() > 0) {
                    Object object = inputStream.readUnshared();
                    if (!(object instanceof Message)) {
                        continue;
                    }
                    Message message = (Message) object;
                    switch (message.getType()) {
                        case StatusEvent: {
                            setGameStatus(((StatusMessage) message).getStatus());
                        }
                    }
                }
                if (!queue.isEmpty()) {
                    outputStream.writeUnshared(queue.poll());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        isRunning = false;
        socket.close();
    }

//    //System
//    public static final int RESPOND_INFORMATION_REQUEST = 1180;//PlayerInformation
//    //Global
//    public static final int PLAYER_JOINED = 2177;
//    public static final int PLAYER_EXITED = 2315;
//    public static final int PLAYER_SEND_MESSAGE = 8376;//Message
//    //Preparing
//    public static final int PLAYER_SET_TEAM = 5827;//int
//    public static final int PLAYER_SET_FORCE_STATE = 8512;//boolean
//    //Running
//    public static final int PLAYER_ORDER_ACTION = 625;//Order
//
////    private final Player.PlayerInformation _information;
//    private final Socket _socket;
//    protected ObjectInputStream _inputStream;
//    protected ObjectOutputStream _outputStream;
////    private volatile GameListenersList _gameListenersList = new GameListenersList();
////    private ModelsManager _modelsManager = new ModelsManager();
////    private final SocketAddress _socketAddress;
//    private final LinkedList<Order> _orders = new LinkedList<>();
//    protected IdentityDocument _playerID;
//    protected GameStatus _gameStatus;
//    protected volatile Game _game;
//    protected volatile Game.GameSetting _setting;
//    protected volatile Player _player;
//    public GameClient(Player information) {
//        _socket = new Socket();
////        _information = information;
//    }
//    public GameClient(SocketAddress socketAddress, Player information) throws IOException {
//        this(information);
//        connect(socketAddress);
//    }
//    public void connect(SocketAddress socketAddress) throws IOException {
//        _socket.connect(socketAddress);
//        _outputStream = new ObjectOutputStream(_socket.getOutputStream());
//        _outputStream.writeObject(VersionResource.getVersion());
//        _inputStream = new ObjectInputStream(_socket.getInputStream());
//        Version version;
//        try {
//            version = (Version) _inputStream.readObject();
//        } catch (ClassNotFoundException e) {
//            throw new SocketException("version error");
//        }
//        if (!version.equals(VersionResource.getVersion())) {
//            throw new SocketException("version mismatch");
//        }
//    }
//    public void start() {
//        _clientThread.start();
//    }
//    public Socket getSocket() {
//        return _socket;
//    }
////    public Player.GamePlayer getSelfPlayer() {
////        return _gameSetting.get;
////    }
//    public void addOrder(int x, int y, Direction direction) {
//        addOrder(new Order(x, y, direction, _playerID));
//    }
//    public void addOrder(Point point, Direction direction) {
//        addOrder(new Order(point, direction, _playerID));
//    }
//    public void addOrder(Order order) {
//        synchronized(_orders) {
//            _orders.add(order);
//        }
//    }
//    public Order getFirstOrder() {
//        synchronized(_orders) {
//            return _orders.peekFirst();
//        }
//    }
//    public Order getLastOrder() {
//        synchronized(_orders) {
//            return _orders.peekFirst();
//        }
//    }
//    public List<Order> getAllOrders() {
//        return Collections.unmodifiableList(_orders);
//    }
//    public Order removeFirstOrder() {
//        synchronized(_orders) {
//            return _orders.pollFirst();
//        }
//    }
//    public Order removeLastOrder() {
//        synchronized(_orders) {
//            return _orders.pollLast();
//        }
//    }
//    public List<Order> removeAllOrders() {
//        synchronized(_orders) {
//            List<Order> orders = new ArrayList<>(getAllOrders());
//            _orders.clear();
//            return orders;
//        }
//    }
//    public boolean getForceState() {
////        return _player.getForceState();
//        return false;
//    }
//    public void setForceState(boolean force) {
////        if (_player.getForceState() != force) {
////            writeGameMessage(new GameMessage(GameClient.PLAYER_SET_FORCE_STATE, force));
////        }
//    }
//    public int getTeam() {
////        return _player.getTeam();
//        return 0;
//    }
//    public void setTeamID(int team) {
////        if (_player.getTeam() != team) {
////            writeGameMessage(new GameMessage(GameClient.PLAYER_SET_TEAM, team));
////        }
//    }
//    public GameStatus getGameState() {
//        return _gameStatus;
//    }
//    public Game getGame() {
//        return _game;
//    }
//    public Game.GameSetting getGameSetting() {
//        return _setting;
//    }
//    public void sendMessage(Message message) {
//        writeGameMessage(new GameMessage(GameClient.PLAYER_SEND_MESSAGE, message));
//    }
//
//    protected final LinkedList<GameMessage> _writingList = new LinkedList<>();
//    private void writeGameMessage(GameMessage message) {
//        synchronized (_writingList) {
//            _writingList.push(message);
//        }
//    }
//    private void clientThreadHandle() throws IOException, ClassNotFoundException {
////        System.out.println("clientThreadHandle");
//        InputStream input = _socket.getInputStream();
//        _outputStream.writeObject(new GameMessage(GameClient.PLAYER_JOINED));
//        while(true) {
//            if (input.available() > 0) {
//                GameMessage gameMessage = (GameMessage) _inputStream.readObject();
////                System.out.println("client:" + gameMessage);
//                switch (gameMessage.getMessageType()) {
//                    //System
//                    case GameService.REQUEST_PLAYER_INFORMATION:
////                        _outputStream.writeObject(new GameMessage(GameClient.RESPOND_INFORMATION_REQUEST, _information));
//                        break;
//                    case GameService.GAME_SET_PLAYER_ID:
//                        _playerID = gameMessage.getData();
//                        _gameClientListenerList.fireListenerEvent(GameClientEvent.createJoinedGameEvent(this));
//                        break;
//                    //Global
//                    case GameService.GAME_STATE_CHANGED:
//                        _gameStatus = gameMessage.getData();
//                        _gameClientListenerList.fireListenerEvent(GameClientEvent.createStateChangedGameEvent(this));
//                        if (_gameStatus == GameStatus.Ended) {
//                            _gameClientListenerList.fireListenerEvent(GameClientEvent.createExitedGameEvent(this));
//                            return;
//                        }
//                        break;
//                    case GameService.PLAYER_SEND_MESSAGE:
//                        Message message = gameMessage.getData();
//                        _gameClientListenerList.fireListenerEvent(GameMessageClientEvent.createSendMessageEvent(this, message));
//                        break;
//                    case GameService.GAME_SETTING_INITIALIZED:
//                        _setting = gameMessage.getData();
//                        _gameClientListenerList.fireListenerEvent(GameClientEvent.createSettingInitialized(this));
//                        break;
//                    //Preparing
//                    case GameService.GAME_PLAYER_FORCE_CHANGED: {
////                        Player.GamePlayer newPlayer = gameMessage.getData();
////                        Player.GamePlayer oldPlayer = Objects.requireNonNull(_setting.getPlayer(newPlayer.getID()));
////                        oldPlayer.setForceStart(newPlayer.getForceState());
////                        _gamePlayerClientListenerList.fireListenerEvent(GamePlayerClientEvent.createPlayerChanged(this, oldPlayer));
//                        break;
//                    }
//                    case GameService.GAME_PLAYER_TEAM_CHANGED: {
////                        Player.GamePlayer newPlayer = gameMessage.getData();
////                        Player.GamePlayer oldPlayer = Objects.requireNonNull(_setting.getPlayer(newPlayer.getID()));
////                        oldPlayer.setTeam(newPlayer.getTeam());
////                        _gamePlayerClientListenerList.fireListenerEvent(GamePlayerClientEvent.createPlayerChanged(this, oldPlayer));
//                        break;
//                    }
//                    //Initializing
//                    case GameService.GAME_MODEL_INITIALIZED:
//                        _game = gameMessage.getData();
//                        _gamePlayerClientListenerList.fireListenerEvent(GamePlayerClientEvent.createGameInitializedEvent(this));
//                        break;
//                    //Running
//                    case GameService.GAME_MAP_UPDATED:
//                        List<Block> updates = gameMessage.getData();
//                        Map map = _game.getMap();
//                        for (Block block : updates) {
//                            map.setBlockAt(block.getX(), block.getY(), block);
//                        }
//                        _gamePlayerClientListenerList.fireListenerEvent(GamePlayerClientEvent.createMapUpdatedEvent(this));
//                        break;
//                    case GameService.REQUEST_PLAYER_ORDER_ACTION:
//                        Order order = removeFirstOrder();
//                        synchronized (_writingList) {
//                            _writingList.push(new GameMessage(GameClient.PLAYER_ORDER_ACTION, order));
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//            if (_playerID != null) {
//                synchronized (_writingList) {
//                    if (!_writingList.isEmpty()) {
//                        _outputStream.writeObject(_writingList.poll());
//                    }
//                }
//            }
//            Thread.yield();
//        }
//    }
//    @Override
//    public void close() throws IOException {
//        _clientThread.interrupt();
//        try {
//            _clientThread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        _socket.close();
//    }
//
//    protected final GameClientListenerList _gameClientListenerList = new GameClientListenerList();
//    public void addClientListener(GameClientListener listener) {
//        _gameClientListenerList.addListener(listener);
//    }
//    public void removeClientListener(GameClientListener listener) {
//        _gameClientListenerList.removeListener(listener);
//    }
//    private final Thread _clientThread = new Thread(() -> {
////        _gameClientListenerList.fireListenerEvent(new GameClientEvent(GameMessage.PLAYER_JOINED, this));
//        try {
//            clientThreadHandle();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
////        _gameClientListenerList.fireListenerEvent(new GameClientEvent(GameMessage.PLAYER_EXITED, this));
//    });
//    //GameClientEvent
//    protected static final class GameClientListenerList extends SyncEventListenerList<GameClientListener, GameClientEvent> {
//        public GameClientListenerList() {
//            super(GameClientListener.class, GameClientEvent.class);
//        }
//        @Override
//        public void fireListenerEvent(GameClientEvent event) {
//            switch (event.getEventType()) {
//                case GameClientEvent.GAME_JOINED:
//                    for (GameClientListener listener : _listenerList) {
//                        listener.gameJoined(event);
//                    }
//                    break;
//                case GameClientEvent.GAME_EXITED:
//                    for (GameClientListener listener : _listenerList) {
//                        listener.gameExited(event);
//                    }
//                    break;
//                case GameClientEvent.GAME_SETTING_INITIALIZED:
//                    for (GameClientListener listener : _listenerList) {
//                        listener.gameSettingChanged(event);
//                    }
//                    break;
//                case GameClientEvent.GAME_STATE_CHANGED:
//                    for (GameClientListener listener : _listenerList) {
//                        listener.gameStatusChanged(event);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//    protected final GameMessageClientListenerList _gameMessageClientListenerList = new GameMessageClientListenerList();
//    public void addMessageClientListener(GameMessageClientListener listener) {
//        _gameMessageClientListenerList.addListener(listener);
//    }
//    public void removeMessageClientListener(GameMessageClientListener listener) {
//        _gameMessageClientListenerList.removeListener(listener);
//    }
//    //GameMessageClientEvent
//    protected static final class GameMessageClientListenerList extends SyncEventListenerList<GameMessageClientListener, GameMessageClientEvent> {
//        public GameMessageClientListenerList() {
//            super(GameMessageClientListener.class, GameMessageClientEvent.class);
//        }
//        @Override
//        public void fireListenerEvent(GameMessageClientEvent event) {
//            switch (event.getEventType()) {
//                case GameMessageClientEvent.PLAYER_JOINED:
//                    for (GameMessageClientListener listener : _listenerList) {
//                        listener.playerJoined(event);
//                    }
//                    break;
//                case GameMessageClientEvent.PLAYER_EXITED:
//                    for (GameMessageClientListener listener : _listenerList) {
//                        listener.playerExited(event);
//                    }
//                    break;
//                case GameMessageClientEvent.PLAYER_SEND_MESSAGE:
//                    for (GameMessageClientListener listener : _listenerList) {
//                        listener.playerSentMessage(event);
//                    }
//                    break;
//                case GameMessageClientEvent.PLAYER_TEAM_CHANGED:
//                    for (GameMessageClientListener listener : _listenerList) {
//                        listener.playerTeamChanged(event);
//                    }
//                    break;
//                case GameMessageClientEvent.PLAYER_SURRENDERED:
//                    for (GameMessageClientListener listener : _listenerList) {
//                        listener.playerSurrendered(event);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//    protected final GamePlayerClientListenerList _gamePlayerClientListenerList = new GamePlayerClientListenerList();
//    public void addPlayerClientListener(GamePlayerClientListener listener) {
//        _gamePlayerClientListenerList.addListener(listener);
//    }
//    public void removePlayerClientListener(GamePlayerClientListener listener) {
//        _gamePlayerClientListenerList.removeListener(listener);
//    }
//    //GamePlayerClientEvent
//    protected static final class GamePlayerClientListenerList extends SyncEventListenerList<GamePlayerClientListener, GamePlayerClientEvent> {
//        public GamePlayerClientListenerList() {
//            super(GamePlayerClientListener.class, GamePlayerClientEvent.class);
//        }
//        @Override
//        public void fireListenerEvent(GamePlayerClientEvent event) {
//            switch (event.getEventType()) {
//                case GamePlayerClientEvent.GAME_INITIALIZED:
//                    for (GamePlayerClientListener listener : _listenerList) {
//                        listener.gameInitialized(event);
//                    }
//                    break;
//                case GamePlayerClientEvent.GAME_MAP_UPDATED:
//                    for (GamePlayerClientListener listener : _listenerList) {
//                        listener.gameMapUpdated(event);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
}
