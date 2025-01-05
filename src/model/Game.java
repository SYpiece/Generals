package model;

import java.io.Serializable;
import java.util.*;

import util.model.IdentityDocument;

public final class Game implements Serializable {
    private int _gameTick;
    public int getGameTick() {
        return _gameTick;
    }
    private final Map _map;
    private final GameSetting _gameSetting;
    public Game(GameSetting setting) {
        _gameSetting = setting;
        _map = new Map(_gameSetting.getMapSetting());
    }
    public GameSetting getGameSetting() {
        return _gameSetting;
    }
    public Set<Player> getAllAlivePlayers() {
        Set<Player> alivePlayers = new HashSet<>();
        _map.forEachBlock(block -> {
            Player owner = block.getOwner();
            if (owner != null) {
                alivePlayers.add(owner);
            }
        });
        return alivePlayers;
    }
    public boolean isPlayerAlive(Player player) {
        return _map.anyBlockMatch(block -> block.getOwner() == player);
    }
    public Set<Team> getAllAliveTeams() {
        Set<Team> aliveTeams = new HashSet<>();
        _map.forEachBlock(block -> {
            Player owner = block.getOwner();
            if (owner != null) {
                Team aliveTeam = owner.getTeam();
                if (aliveTeam != null) {
                    aliveTeams.add(aliveTeam);
                }
            }
        });
        return aliveTeams;
    }
    public boolean isTeamAlive(Team team) {
        return _map.anyBlockMatch(block -> {
            Player owner = block.getOwner();
            if (owner != null) {
                Team aliveTeam = owner.getTeam();
                return aliveTeam == team;
            }
            return false;
        });
    }
    public int getMapWidth() {
        return _map.getWidth();
    }
    public int getMapHeight() {
        return _map.getHeight();
    }
    public Map getMap() {
        return _map;
    }
    public Set<Block> update(List<Order> orders) {
        Set<Block> changes = new HashSet<>();
        _map.forEachBlock(block -> {
            block.update(this);
            changes.add(block);
        });
        for (Order order : orders) {
            Block fromBlock = _map.getBlockAt(order.fromPoint()), toBlock = _map.getBlockAt(order.toPoint());
            if (!fromBlock.getOwner().equals(order.getOperator())) {
                continue;
            }
            if (fromBlock.getPeople() > 1) {
                if (!changes.contains(fromBlock)) {
                    changes.add(fromBlock);
                }
                if (!changes.contains(toBlock)) {
                    changes.add(toBlock);
                }
                toBlock.setPeople(toBlock.getPeople() - fromBlock.getPeople() + 1);
                fromBlock.setPeople(1);
                if (toBlock.getPeople() < 0) {
                    if (toBlock instanceof CityBlock && ((CityBlock) toBlock).isCrown()) {
                        _map.forEachBlock(block -> {
                            if (block.getOwner() == toBlock.getOwner()) {
                                block.setOwner(fromBlock.getOwner());
                                if (!changes.contains(block)) {
                                    changes.add(block);
                                }
                            }
                        });
                        ((CityBlock) toBlock).beenCaptured();
                    }
                    toBlock.setOwner(fromBlock.getOwner());
                }
            }
        }
        return Collections.unmodifiableList(changes);
    }
    public static final class GameSetting implements Serializable {
        private final Map.MapSetting _mapSetting;
        private int _gameSpeed;
        public int getMapWidth() {
            return _mapSetting.getMapWidth();
        }
        public void setMapWidth(int width) {
            _mapSetting.setMapWidth(width);
        }
        public int getMapHeight() {
            return _mapSetting.getMapHeight();
        }
        public void setMapHeight(int height) {
            _mapSetting.setMapHeight(height);
        }
        public Set<Player> getAllPlayers() {
            return _mapSetting.getAllPlayers();
        }
        public boolean addPlayer(Player player) {
            return _mapSetting.addPlayer(player);
        }
        public boolean removePlayer(Player player) {
            return _mapSetting.removePlayer(player);
        }
        public int playerCount() {
            return _mapSetting.playerCount();
        }
        public int teamCount() {
            return getAllTeams().size();
        }
        public Set<Team> getAllTeams() {
            Set<Team> teams = new HashSet<>();
            for (Player player : _mapSetting.getAllPlayers()) {
                teams.add(player.getTeam());
            }
            return Collections.unmodifiableSet(teams);
        }
        public void setGameSpeed(int speed) {
            _gameSpeed = speed;
        }
        public int getGameSpeed() {
            return _gameSpeed;
        }
        public Map.MapSetting getMapSetting() {
            return _mapSetting;
        }
        public GameSetting(int mapWidth, int mapHeight) {
            this(new Map.MapSetting(mapWidth, mapHeight), 500);
        }
        public GameSetting(int mapWidth, int mapHeight, int gameSpeed) {
            this(new Map.MapSetting(mapWidth, mapHeight), gameSpeed);
        }
        public GameSetting(int mapWidth, int mapHeight, Set<Player> players, int gameSpeed) {
            this(new Map.MapSetting(mapWidth, mapHeight, players), gameSpeed);
        }
        private GameSetting(Map.MapSetting mapSetting, int gameSpeed) {
            _mapSetting = mapSetting;
            _gameSpeed = gameSpeed;
        }
    }
}
