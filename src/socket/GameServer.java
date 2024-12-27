package socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.*;

import model.*;
import resource.color.Color;
import socket.event.*;

public class GameServer implements Closeable {
    private volatile Game _game;
    private final List<GameService> _gameServices = new ArrayList<>();
    private volatile GameStatus _gameStatus = GameStatus.Unknown;
    private final Game.GameSetting _gameSetting = new Game.GameSetting(10, 10, new ArrayList<>(), 500);
    private final SocketAddress _socketAddress;
    private final Thread _serverThread = new Thread(this::serverThreadHandle);
    private final ServerSocket _serverSocket;
    public GameServer(SocketAddress socketAddress) throws IOException {
        _socketAddress = socketAddress;
        _serverSocket = new ServerSocket();
    }
    public void start() throws IOException {
        _serverThread.start();
        _serverSocket.bind(_socketAddress);
        _serverAcceptationThread.start();
    }
    private final GameServiceAdapter _serviceAdapter = new GameServiceAdapter() {
        @Override
        public void playerJoined(GameServiceEvent event) {
            GameService service = event.getService();
            _gameSetting.addPlayer(service.getPlayer());
            service.writeGameSetting(_gameSetting);
            synchronized (_gameServices) {
                _gameServices.add(service);
                for (GameService gameService : _gameServices) {
                    if (gameService != service) {
                        gameService.writeJoinedPlayer(service.getPlayer());
                    }
                }
            }
        }
        @Override
        public void playerExited(GameServiceEvent event) {
            GameService service = event.getService();
            _gameSetting.removePlayer(service.getPlayer());
            synchronized (_gameServices) {
                _gameServices.remove(event.getService());
                for (GameService gameService : _gameServices) {
                    gameService.writeExitedPlayer(service.getPlayer());
                }
            }
        }
    };
    private void setGameState(GameStatus state) {
        _gameStatus = state;
        synchronized (_gameServices) {
            for (GameService service : _gameServices) {
                service.setGameStatus(state);
            }
        }
    }
    private final GameMessageServiceAdapter _messageServiceAdapter = new GameMessageServiceAdapter() {
        @Override
        public void playerMessaged(GameMessageServiceEvent event) {
            synchronized (_gameServices) {
                for (GameService service : _gameServices) {
                    service.writePlayerMessage(event.getMessage());
                }
            }
        }
    };
    private final GamePlayerServiceAdapter _playerServiceAdapter = new GamePlayerServiceAdapter() {
        @Override
        public void playerChangedForceState(GamePlayerServiceEvent event) {
            synchronized (_gameServices) {
                for (GameService service : _gameServices) {
                    if (service != event.getService()) {
                        service.writeForceChangedPlayer(event.getService().getPlayer());
                    }
                }
            }
            if (_gameSetting.realPlayerCount() <= _gameSetting.forcePlayerCount()) {
                setGameState(GameStatus.Initializing);
            }
        }
        @Override
        public void playerTeamChanged(GamePlayerServiceEvent event) {
            synchronized (_gameServices) {
                for (GameService service : _gameServices) {
                    if (service != event.getService()) {
                        service.writeTeamChangedPlayer(event.getService().getPlayer());
                    }
                }
            }
        }
    };
    private final Thread _serverAcceptationThread = new Thread(this::serverAcceptationThreadHandle);
    private void serverAcceptationThreadHandle() {
//        System.out.println("serverAcceptationThreadHandle");
        while (true) {
            GameService service;
            try {
                service = new GameService(_serverSocket.accept());
            } catch (IOException e) {
                continue;
            }
            service.addServiceListener(_serviceAdapter);
            service.addMessageServiceListener(_messageServiceAdapter);
            service.addPlayerServiceListener(_playerServiceAdapter);
            service.start();
            Thread.yield();
        }
    }
    private void serverThreadHandle() {
        _gameStatus = GameStatus.Preparing;
        while (_gameStatus == GameStatus.Preparing) {
            Thread.yield();
        }
        _game = new Game(_gameSetting);
        synchronized (_gameServices) {
            for (GameService service : _gameServices) {
                service.writeGameModel(_game);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setGameState(GameStatus.Running);
        while (_gameStatus == GameStatus.Running) {
            List<Order> orders = new LinkedList<>();
            synchronized (_gameServices) {
                for (GameService service : _gameServices) {
                    orders.add(service.requestOrder());
                }
            }
            List<Block> changes = _game.update(orders);
            synchronized (_gameServices) {
                for (GameService service : _gameServices) {
                    service.writeMapUpdates(changes);
                }
            }
            try {
                Thread.sleep(_gameSetting.getGameSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<GameService> getGameServices() {
        return Collections.unmodifiableList(_gameServices);
    }
    @Override
    public void close() throws IOException {
        _serverThread.interrupt();
        _serverAcceptationThread.interrupt();
        synchronized (_gameServices) {
            for (GameService service : _gameServices) {
                service.close();
            }
        }
        try {
            _serverThread.join();
            _serverAcceptationThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}