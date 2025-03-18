package model;

public final class City extends BlockBase implements CityBlock {
    private static final int updateCount = 2;
    private boolean isCrown;

	public City(int x, int y) {
        this(x, y, false);
    }

    public City(int x, int y, boolean isCrown) {
        super(x, y);
        this.isCrown = isCrown;
    }

    @Override
    public boolean isCrown() {
        return isCrown;
    }

    @Override
    public void setIsCrown(boolean isCrown) {
        this.isCrown = isCrown;
    }

    @Override
    public boolean simulate(Game game) {
        if (getOwner() != null && game.getTick() % updateCount == 0) {
            setPeople(getPeople() + 1);
            return true;
        } else {
            return false;
        }
    }
}
