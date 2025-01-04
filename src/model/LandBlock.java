package model;

public final class LandBlock extends Block {
	private static final int updateCount = 50;
    public LandBlock(int x, int y) {
        super(x, y);
    }
//    protected Land(IdentityDocument id) {
//        super(id);
//    }
//    @Override
//    public Image getImage(boolean isFound) {
//        return ImageResource.emptyImage;
//    }
//    @Override
//    public Color getColor(boolean isFound) {
//        if (isFound) {
//            if (_owner == null) {
//                return new Color(0xe3e3e3);
//            } else {
//                return _owner.getColor();
//            }
//        } else {
//            return Color.LIGHT_GRAY;
//        }
//    }
    @Override
    public boolean update(Game game) {
        if (getOwner() != null && (game.getGameTick() % updateCount) == 0) {
            _people++;
            return true;
        } else {
            return false;
        }
    }
//    @Override
//    public LandBlockSummary getSummary() {
//        return new LandBlockSummary(this);
//    }
//    public static final class LandBlockSummary extends BlockSummary {
//        public LandBlockSummary(LandBlock block) {
//            super(block);
//        }
//    }
}
