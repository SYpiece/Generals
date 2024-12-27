package model;

import java.awt.Color;
import java.awt.Image;

import resource.image.ImageResource;

public final class LandBlock extends Block {
	private static final int updateCount = 25;
    private int click;
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
        if (++click >= updateCount) {
            if (_ownerID != null) {
                _people++;
                click = 0;
                return true;
            }
            click = 0;
        }
        return false;
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
