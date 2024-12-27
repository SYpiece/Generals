package model;

import java.awt.Color;
import java.awt.Image;

import resource.image.ImageResource;

public class MountainBlock extends ObstacleBlock {
	public MountainBlock(int x, int y) {
        super(x, y);
    }
//    protected Mountain(IdentityDocument id) {
//        super(id);
//    }
//    @Override
//    public Image getImage(boolean isFound) {
//        if (isFound) {
//            return ImageResource.mountainImage;
//        } else {
//            return ImageResource.obstacleImage;
//        }
//    }
//    @Override
//    public Color getColor(boolean isFound) {
//        return Color.LIGHT_GRAY;
//    }
    @Override
    public boolean update(Game game) {
        return false;
    }

//    @Override
//    public MountainBlockSummary getSummary() {
//        return new MountainBlockSummary(this);
//    }
//    public static class MountainBlockSummary extends BlockSummary {
//        public MountainBlockSummary(MountainBlock mountainBlock) {
//            super(mountainBlock);
//        }
//    }
}
