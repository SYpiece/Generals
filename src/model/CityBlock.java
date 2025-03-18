package model;

public interface CityBlock extends Block {
    String
            isCrownKey = "IS_CROWN";

    boolean isCrown();

    void setIsCrown(boolean isCrown);

    @Override
    default BlockType getBlockType() {
        return BlockType.City;
    }

    @Override
    default ImageType getImageType(boolean shown) {
        return isCrown() ?
                (shown ? ImageType.Crown : ImageType.Land) :
                (shown ? ImageType.City : ImageType.Obstacle);
    }

    @Override
    default Color getBackground(boolean shown) {
        return Color.GRAY;
    }

    @Override
    default Configuration getConfiguration() {
       return Block.super.getConfiguration()
               .with(isCrownKey, this.isCrown());
    }

    @Override
    default void apply(Configuration configuration) {
        Block.super.apply(configuration);
        if (configuration.contains(isCrownKey)) {
            setIsCrown(configuration.get(isCrownKey));
        }
    }
}
