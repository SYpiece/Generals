package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Map implements Serializable {
	private static final int density = 6;
	private final Block[][] mapBlocks;
	protected final MapSetting _mapSetting;
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
					mapBlocks[x][y].setOwner(setting.getBot().getID());
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
			mapBlocks[x][y].setOwner(realPlayers.get(i).getID());
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
		private final Set<Player> _players;
		private final Player _bot;
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
		public Set<Player> getPlayers() {
			return Collections.unmodifiableSet(_players);
		}
		public Set<Player> getRealPlayers() {
			Set<Player> realPlayers = new HashSet<>(_players.size());
			for (Player player : _players) {
				if (player != _bot) {
					realPlayers.add(player);
				}
			}
			return Collections.unmodifiableSet(realPlayers);
		}
		public void addPlayer(Player player) {
			_players.add(player);
		}
		public void removePlayer(Player player) {
			_players.remove(player);
		}
		public void removeAllPlayer(Set<Player> players) {
			for (Player player : players) {
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
