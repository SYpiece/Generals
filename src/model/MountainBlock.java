package model;

public interface MountainBlock extends Block {
    @Override
    default BlockType getBlockType() {
        return BlockType.Mountain;
    }

    @Override
    default ImageType getImageType(boolean shown) {
        return shown ? ImageType.Mountain : ImageType.Obstacle;
    }

    @Override
    default Color getBackground(boolean shown) {
        return Color.GRAY;
    }
}
