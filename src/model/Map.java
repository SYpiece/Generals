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
	public static final class MapSetting implements Serializable {
		private int _width, _height;
		private final Set<Player> _players;
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
		public Set<Player> getAllPlayers() {
			return Collections.unmodifiableSet(_players);
		}
		public boolean addPlayer(Player player) {
			return _players.add(player);
		}
		public boolean removePlayer(Player player) {
			return _players.remove(player);
		}
		public boolean removeAllPlayer(Set<Player> players) {
			return _players.removeAll(players);
		}
		public int playerCount() {
			return _players.size();
		}
		public MapSetting(int width, int height) {
			this(width, height, new HashSet<>());
		}
		public MapSetting(int width, int height, Set<Player> players) {
			_width = width;
			_height = height;
			_players = players;
		}
	}
}
