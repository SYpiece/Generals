package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import util.model.IdentityDocument;

public final class Game implements Serializable {
    private int _gameTick;
    public int getGameTick() {
        return _gameTick;
    }
    private final Map _map;
    private final GameSetting _gameSetting;
    public Player getBot() {
        return _gameSetting.getBot();
    }
//    public List<Team> getTeams() {
//        return _gameSetting.getTeams();
//    }
    public Game(GameSetting setting) {
//        _id = IdentityDocument.createID();
        _gameSetting = setting.clone();
        _map = new Map(_gameSetting.getMapSetting());
    }
//    protected Game(IdentityDocument id) {
//        _id = id;
//    }
    public GameSetting getGameSetting() {
        return _gameSetting;
    }
//    public List<Player.GamePlayer> getPlayers() {
//        return _gameSetting.getPlayers();
//    }
    public List<Player.GamePlayer> getAlivePlayers() {
        List<IdentityDocument> alivePlayersID = new ArrayList<>();
        _map.forEachBlock(block -> {
            IdentityDocument id = block.getOwner();
            if (id != null && id != _gameSetting.getBot().getID() && !alivePlayersID.contains(id)) {
                alivePlayersID.add(id);
            }
        });
        return _gameSetting.getAllPlayer(alivePlayersID);
    }
    public boolean isPlayerAlive(Player.GamePlayer player) {
        return _map.anyBlockMatch(block -> block.getOwner() == player.getID());
    }
    public List<Integer> getAliveTeams() {
        List<IdentityDocument> aliveTeamsID = new ArrayList<>();
        _map.forEachBlock(block -> {
            IdentityDocument id = block.getOwner();
            if (id != null && id != _gameSetting.getBot().getID() && !aliveTeamsID.contains(id)) {
                aliveTeamsID.add(id);
            }
        });
        return _gameSetting.getAllPlayerTeam(aliveTeamsID);
    }
    public boolean isTeamAlive(int team) {
        return _map.anyBlockMatch(block -> block.getOwner() != null && _gameSetting.getPlayerTeam(block.getOwner()) == team);
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
    public List<Block> update(List<Order> orders) {
        List<Block> changes = new LinkedList<>();
        _map.forEachBlock(block -> {
            block.update(this);
            changes.add(block);
        });
        for (Order order : orders) {
            if (order == null) {
                continue;
            }
            Block fromBlock = _map.getBlockAt(order.fromPoint()), toBlock = _map.getBlockAt(order.toPoint());
            if (!fromBlock.getOwner().equals(toBlock.getOwner())) {
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
    public static final class GameSetting implements Serializable, Cloneable {
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
        public void addPlayer(Player.GamePlayer player) {
            _mapSetting.addPlayer(player);
        }
        public void removePlayer(Player.GamePlayer player) {
            _mapSetting.removePlayer(player);
        }
        public void removeTeam(int team) {
            _mapSetting.removeAllPlayer(getAllPlayer(team));
        }
        public int playerCount() {
            return _mapSetting.playerCount();
        }
        public int realPlayerCount() {
            return _mapSetting.realPlayerCount();
        }
        public int forcePlayerCount() {
            return _mapSetting.forcePlayerCount();
        }
        public int teamCount() {
            return getTeams().size();
        }
        public List<Integer> getTeams() {
            List<Integer> teams = new ArrayList<>();
            for (Player.GamePlayer player : _mapSetting.getPlayers()) {
                int team = player.getTeam();
                if (player.isReal() && !teams.contains(team)) {
                    teams.add(team);
                }
            }
            return Collections.unmodifiableList(teams);
        }
        public List<Player.GamePlayer> getPlayers() {
            return _mapSetting.getPlayers();
        }
        public List<Player.GamePlayer> getRealPlayers() {
            return _mapSetting.getRealPlayers();
        }
        public List<Player.GamePlayer> getForcePlayers() {
            return _mapSetting.getForcePlayers();
        }
        public Player.GamePlayer getBot() {
            return _mapSetting.getBot();
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
        public GameSetting() {
            this(new Map.MapSetting(), 500);
        }
        public GameSetting(int mapWidth, int mapHeight) {
            this(new Map.MapSetting(mapWidth, mapHeight), 500);
        }
        public GameSetting(int mapWidth, int mapHeight, List<Player.GamePlayer> players, int gameSpeed) {
            this(new Map.MapSetting(mapWidth, mapHeight, players), gameSpeed);
        }
        private GameSetting(Map.MapSetting mapSetting, int gameSpeed) {
            _mapSetting = mapSetting;
            _gameSpeed = gameSpeed;
        }
        public Player.GamePlayer getPlayer(IdentityDocument id) {
            if (id.equals(_mapSetting.getBot().getID())) {
                return _mapSetting.getBot();
            }
            for (Player.GamePlayer player : _mapSetting.getPlayers()) {
                if (id.equals(player.getID())) {
                    return player;
                }
            }
            return null;
        }
        public int getPlayerTeam(IdentityDocument id) {
            Player.GamePlayer player = getPlayer(id);
            if (player == null) {
                return -1;
            } else {
                return player.getTeam();
            }
        }
        public List<Player.GamePlayer> getAllPlayer(List<IdentityDocument> ids) {
            List<Player.GamePlayer> players = new ArrayList<>(ids.size());
            for (IdentityDocument id : ids) {
                Player.GamePlayer player = getPlayer(id);
                if (player != null && !players.contains(player)) {
                    players.add(getPlayer(id));
                }
            }
            return Collections.unmodifiableList(players);
        }
        public List<Integer> getAllPlayerTeam(List<IdentityDocument> ids) {
            List<Integer> playerTeams = new ArrayList<>();
            for (IdentityDocument id : ids) {
                Player.GamePlayer player = getPlayer(id);
                if (player != null && player.isReal() && !playerTeams.contains(player.getTeam())) {
                    playerTeams.add(player.getTeam());
                }
            }
            return Collections.unmodifiableList(playerTeams);
        }
        public List<Player.GamePlayer> getAllPlayer(int team) {
            List<Player.GamePlayer> players = new ArrayList<>();
            for (Player.GamePlayer player : _mapSetting.getPlayers()) {
                if (player.isReal() && player.getTeam() == team) {
                    players.add(player);
                }
            }
            return Collections.unmodifiableList(players);
        }
        @Override
        public GameSetting clone() {
            return new GameSetting(_mapSetting, _gameSpeed);
        }
    }
}
