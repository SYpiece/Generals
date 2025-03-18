package model;

import java.util.List;
import java.util.Set;

public interface Map extends Configurable {
    String
            blocksKey = "BLOCKS";

    List<List<Block>> getBlocks();

    void setBlocks(List<List<Block>> blocks);

    int getWidth();

    int getHeight();

    Block getBlock(int x, int y);

    void setBlock(int x, int y, Block block);

    @Override
    default Configuration getConfiguration() {
        return new Configuration()
                .with(blocksKey, getBlocks());
    }

    @Override
    default void apply(Configuration configuration) {
        if (configuration.contains(blocksKey)) {
            setBlocks(configuration.get(blocksKey));
        }
    }
}
