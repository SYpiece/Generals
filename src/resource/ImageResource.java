package resource;

import javafx.scene.image.Image;
import model.Block;

public final class ImageResource extends Resource {
    private static final Image _cityImage = readImage("/image/city.png");
    private static final Image _crownImage = readImage("/image/crown.png");
    private static final Image _mountainImage = readImage("/image/mountain.png");
    private static final Image _obstacleImage = readImage("/image/obstacle.png");
    public static Image getBlockImage(Block block, boolean isFound) {
        switch (block.getType()) {
            case City:
                if (isFound) {
                    return _cityImage;
                } else {
                    return _obstacleImage;
                }
            case Crown:
                if (isFound) {
                    return _crownImage;
                } else {
                    return null;
                }
            case Mountain:
                if (isFound) {
                    return _mountainImage;
                } else {
                    return _obstacleImage;
                }
            case Land:
            default:
                return null;
        }
    }
    private ImageResource() {}
}
