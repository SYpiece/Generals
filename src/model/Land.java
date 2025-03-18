package model;

public final class Land extends BlockBase implements LandBlock {
	private static final int updateCount = 50;

    public Land(int x, int y) {
        super(x, y);
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
