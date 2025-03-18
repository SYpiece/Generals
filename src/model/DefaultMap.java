package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultMap implements Map {
	private static final int density = 6;
	protected final MapSetting _mapSetting;

	protected List<List<Block>> blocks;

	public DefaultMap(MapSetting setting) {
		mapBlockBases = new BlockBase[setting.getMapWidth()][setting.getMapHeight()];
		_mapSetting = setting;
		Random random = new Random();
		for (int i = 0; i < setting.getMapWidth(); i++) {
			for (int j = 0; j < setting.getMapHeight(); j++) {
				int rand = random.nextInt(6);
				if (rand == 0) {
					mapBlockBases[i][j] = new Mountain(i, j);
				} else {
					mapBlockBases[i][j] = new Land(i, j);
				}
			}
		}
		for (int i = 0; i < setting.getMapWidth(); i += density) {
			for (int j = 0; j < setting.getMapHeight(); j += density) {
				int x = i + random.nextInt(density);
				int y = j + random.nextInt(density);
				try {
					mapBlockBases[x][y] = new City(i, j);
					mapBlockBases[x][y].setPeople(random.nextInt(10) + 40);
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		Point[] crownPoints = new Point[setting.playerCount()];
		Set<DefaultPlayer> realPlayers = _mapSetting.getAllPlayers();
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
			mapBlockBases[x][y] = new City(x, y, true);
			mapBlockBases[x][y].setOwner(realPlayers.get(i).getID());
		}
	}
	public MapSetting getMapSetting() {
		return _mapSetting;
	}

	public List<List<Block>> getBlocks() {
		List<List<Block>> blocks = new ArrayList<>(blocks.size());
		for (List<Block> row : blocks) {
			blocks.add(Collections.unmodifiableList(row));
		}
		return Collections.unmodifiableList(blocks);
	}

	public void setBlocks(List<List<Block>> blocks) {
		if (blocks != null) {
			Iterator<List<Block>> iterator = blocks.iterator();
			if (iterator.hasNext()) {
				for (List<Block> rowA = iterator.next(), rowB; iterator.hasNext(); rowA = rowB) {
					rowB = iterator.next();
					if (rowA.size() != rowB.size()) {
						throw new IllegalArgumentException();
					}
				}
			}
		}
		this.blocks = blocks;
	}

	@Override
	public int getWidth() {
		return blocks.get(0).size();
	}

	@Override
	public int getHeight() {
		return blocks.size();
	}

	@Override
	public Block getBlock(int x, int y) {
		return blocks.get(y).get(x);
	}

	@Override
	public void setBlock(int x, int y, Block block) {
		blocks.get(y).set(x, block);
	}

	public void forEachBlock(Consumer<BlockBase> action) {
		for (BlockBase[] blockBases : mapBlockBases) {
			for (BlockBase blockBase : blockBases) {
				action.accept(blockBase);
			}
		}
	}
	public boolean anyBlockMatch(Predicate<BlockBase> predicate) {
		return Arrays.stream(mapBlockBases).anyMatch(blocks -> Arrays.stream(blocks).anyMatch(predicate));
	}
//	private IdentityDocument _id = IdentityDocument.createID();
//	@Override
//	public IdentityDocument getID() {
//		return _id;
//	}
	public static final class MapSetting implements Serializable {
		private int _width, _height;
		private final Set<DefaultPlayer> _players;
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
		public Set<DefaultPlayer> getAllPlayers() {
			return Collections.unmodifiableSet(_players);
		}
		public boolean addPlayer(DefaultPlayer player) {
			return _players.add(player);
		}
		public boolean removePlayer(DefaultPlayer player) {
			return _players.remove(player);
		}
		public boolean removeAllPlayer(Set<DefaultPlayer> players) {
			return _players.removeAll(players);
		}
		public int playerCount() {
			return _players.size();
		}
		public MapSetting(int width, int height) {
			this(width, height, new HashSet<>());
		}
		public MapSetting(int width, int height, Set<DefaultPlayer> players) {
			_width = width;
			_height = height;
			_players = players;
		}
	}
}
