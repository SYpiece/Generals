package model;

import java.io.Serializable;
import java.util.*;

public final class DefaultGame implements Game, Serializable {
    private int tick;
    private Map map;
    private final GameSetting _gameSetting;

    public DefaultGame(GameSetting setting) {
        _gameSetting = setting;
        map = new DefaultMap(_gameSetting.getMapSetting());
    }

    public GameSetting getGameSetting() {
        return _gameSetting;
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public Set<Block> simulate(List<Order> orders) {
        Set<Block> changes = new HashSet<>();

        for (List<Block> row : map.getBlocks()) {
            for (Block block : row) {
                if (block.simulate(this)) {
                    changes.add(block);
                }
            }
        }

        for (Order order : orders) {
            Block fromBlock = map.getBlock(order.fromX(), order.fromY()), toBlock = map.getBlock(order.toX(), order.toY());
            if (!fromBlock.getOwner().equals(order.getOperator())) {
                continue;
            }
            if (fromBlock.getPeople() > 1) {
                changes.add(fromBlock);
                changes.add(toBlock);
                toBlock.setPeople(toBlock.getPeople() - fromBlock.getPeople() + 1);
                fromBlock.setPeople(1);
                if (toBlock.getPeople() < 0) {
                    if (toBlock instanceof City && ((City) toBlock).isCrown()) {
                        for (List<Block> row : map.getBlocks()) {
                            for (Block block : row) {
                                if (block.getOwner().equals(toBlock.getOwner())) {
                                    block.setOwner(fromBlock.getOwner());
                                    changes.add(block);
                                }
                            }
                        }
                        ((City) toBlock).setIsCrown(false);
                    }
                    toBlock.setOwner(fromBlock.getOwner());
                }
            }
        }

        setTick(getTick() + 1);

        return Collections.unmodifiableSet(changes);
    }

    @Override
    public void apply(Set<Block> blocks) {
        for (Block block : blocks) {
            map.setBlock(block.getX(), block.getY(), block);
        }
    }

    public static final class GameSetting implements Serializable {
        private final DefaultMap.MapSetting _mapSetting;
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
        public Set<DefaultPlayer> getAllPlayers() {
            return _mapSetting.getAllPlayers();
        }
        public boolean addPlayer(DefaultPlayer player) {
            return _mapSetting.addPlayer(player);
        }
        public boolean removePlayer(DefaultPlayer player) {
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
            for (DefaultPlayer player : _mapSetting.getAllPlayers()) {
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
        public DefaultMap.MapSetting getMapSetting() {
            return _mapSetting;
        }
        public GameSetting(int mapWidth, int mapHeight) {
            this(new DefaultMap.MapSetting(mapWidth, mapHeight), 500);
        }
        public GameSetting(int mapWidth, int mapHeight, int gameSpeed) {
            this(new DefaultMap.MapSetting(mapWidth, mapHeight), gameSpeed);
        }
        public GameSetting(int mapWidth, int mapHeight, Set<DefaultPlayer> players, int gameSpeed) {
            this(new DefaultMap.MapSetting(mapWidth, mapHeight, players), gameSpeed);
        }
        private GameSetting(DefaultMap.MapSetting mapSetting, int gameSpeed) {
            _mapSetting = mapSetting;
            _gameSpeed = gameSpeed;
        }
    }
}
