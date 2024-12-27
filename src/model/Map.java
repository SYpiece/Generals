package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Map implements Serializable {
	private final Block[][] mapBlocks;
	private transient static final int density = 6;
	protected final MapSetting _mapSetting;
//	public Map(int width, int height, Player[] players, Player bot) {
	public Map(MapSetting setting) {
		mapBlocks = new Block[setting.getMapWidth()][setting.getMapHeight()];
		_mapSetting = setting.clone();
		Random random = new Random();
		for (int i = 0; i < setting.getMapWidth(); i++) {
			for (int j = 0; j < setting.getMapHeight(); j++) {
				int rand = random.nextInt(6);
				if (rand == 0) {
					mapBlocks[i][j] = new MountainBlock(i, j);
				} else {
					mapBlocks[i][j] = new LandBlock(i, j);
				}
			}
		}
		for (int i = 0; i < setting.getMapWidth(); i += density) {
			for (int j = 0; j < setting.getMapHeight(); j += density) {
				int x = i + random.nextInt(density);
				int y = j + random.nextInt(density);
				try {
					mapBlocks[x][y] = new CityBlock(i, j);
					mapBlocks[x][y].setOwnerID(setting.getBot().getID());
					mapBlocks[x][y].setPeople(random.nextInt(10) + 40);
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		Point[] crownPoints = new Point[setting.realPlayerCount()];
		List<Player.GamePlayer> realPlayers = _mapSetting.getRealPlayers();
		for (int i = 0; i < realPlayers.size(); i++) {
			int minDistance;
			int x;
			int y;
			do {
				minDistance = Integer.MAX_VALUE;
				x = random.nextInt(setting.getMapWidth());
				y = random.nextInt(setting.getMapHeight());
				for (int j = 0; j < i; j++) {
					minDistance = Math.min(minDistance, Math.abs(crownPoints[j].x - x) + Math.abs(crownPoints[j].y - y));
				}
			} while (minDistance <= 15);
			crownPoints[i] = new Point(x, y);
			mapBlocks[x][y] = new CityBlock(x, y, true);
			mapBlocks[x][y].setOwnerID(realPlayers.get(i).getID());
		}
	}
	public MapSetting getMapSetting() {
		return _mapSetting;
	}
	public int getWidth() {
		return mapBlocks.length;
	}
	public int getHeight() {
		return mapBlocks[0].length;
	}
	public Block getBlockAt(int x, int y) {
		return mapBlocks[x][y];
	}
	public Block getBlockAt(Point point) {
		return mapBlocks[point.x][point.y];
	}
	public void setBlockAt(int x, int y, Block block) {
		mapBlocks[x][y] = block;
	}
	public void setBlockAt(Point point, Block block) {
		mapBlocks[point.x][point.y] = block;
	}
	public void forEachBlock(Consumer<Block> action) {
		for (Block[] blocks : mapBlocks) {
			for (Block block : blocks) {
				action.accept(block);
			}
		}
	}
	public boolean anyBlockMatch(Predicate<Block> predicate) {
		return Arrays.stream(mapBlocks).anyMatch(blocks -> Arrays.stream(blocks).anyMatch(predicate));
	}
//	private IdentityDocument _id = IdentityDocument.createID();
//	@Override
//	public IdentityDocument getID() {
//		return _id;
//	}
	public static final class MapSetting implements Serializable, Cloneable {
		private int _width, _height;
		private final List<Player.GamePlayer> _players;
		private final Player.GamePlayer _bot;
		public int getMapWidth() {
			return _width;
		}
		public void setMapWidth(int width) {
			_width = width;
		}
		public int getMapHeight() {
			return _height;
		}
		public void setMapHeight(int height) {
			_height = height;
		}
		public List<Player.GamePlayer> getPlayers() {
			return Collections.unmodifiableList(_players);
		}
		public List<Player.GamePlayer> getRealPlayers() {
			List<Player.GamePlayer> realPlayers = new ArrayList<>(_players.size());
			for (Player.GamePlayer player : _players) {
				if (player.isReal()) {
					realPlayers.add(player);
				}
			}
			return Collections.unmodifiableList(realPlayers);
		}
		public List<Player.GamePlayer> getForcePlayers() {
			List<Player.GamePlayer> realPlayers = new ArrayList<>(_players.size());
			for (Player.GamePlayer player : _players) {
				if (player.getForceState()) {
					realPlayers.add(player);
				}
			}
			return Collections.unmodifiableList(realPlayers);
		}
		public void addPlayer(Player.GamePlayer player) {
			_players.add(player);
		}
		public void removePlayer(Player.GamePlayer player) {
			_players.remove(player);
		}
		public void removeAllPlayer(List<Player.GamePlayer> players) {
			for (Player.GamePlayer player : players) {
				_players.remove(player);
			}
		}
		public int playerCount() {
			return _players.size();
		}
		public int realPlayerCount() {
			int realPlayerCount = 0;
			for (Player.GamePlayer player : _players) {
				if (player.isReal()) {
					realPlayerCount++;
				}
			}
			return realPlayerCount;
		}
		public int forcePlayerCount() {
			int forcePlayerCount = 0;
			for (Player.GamePlayer player : _players) {
				if (player.getForceState()) {
					forcePlayerCount++;
				}
			}
			return forcePlayerCount;
		}
		public Player.GamePlayer getBot() {
			return _bot;
		}
		public MapSetting() {
			this(20, 20, new ArrayList<>());
		}
		public MapSetting(int width, int height) {
			this(width, height, Collections.emptyList());
		}
		public MapSetting(int width, int height, List<Player.GamePlayer> players) {
			this(width, height, new ArrayList<>(players), Player.GamePlayer.createBot());
		}
		private MapSetting(int width, int height, List<Player.GamePlayer> players, Player.GamePlayer bot) {
			_width = width;
			_height = height;
			_players = players;
			_bot = bot;
		}
		@Override
		public MapSetting clone() {
            return new MapSetting(_width, _height, new ArrayList<>(_players), _bot);
		}
	}
}
