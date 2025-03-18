package model;

public class Mountain extends BlockBase implements MountainBlock {
	public Mountain(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean simulate(Game game) {
        return false;
    }
}
