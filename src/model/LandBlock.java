package model;

public interface LandBlock extends Block {
    @Override
    default BlockType getBlockType() {
        return BlockType.Land;
    }

    @Override
    default ImageType getImageType(boolean shown) {
        return ImageType.Land;
    }

    @Override
    default Color getBackground(boolean shown) {
        return Color.GRAY;
    }
}
