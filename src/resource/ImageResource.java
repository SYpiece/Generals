package resource;

import javafx.scene.image.Image;
import model.Block;

import java.util.Objects;

public final class ImageResource {
    private static final Image _cityImage = new Image(Objects.requireNonNull(ImageResource.class.getResourceAsStream("/image/city.png")));
    private static final Image _crownImage = new Image(Objects.requireNonNull(ImageResource.class.getResourceAsStream("/image/crown.png")));
    private static final Image _mountainImage = new Image(Objects.requireNonNull(ImageResource.class.getResourceAsStream("/image/mountain.png")));
    private static final Image _obstacleImage = new Image(Objects.requireNonNull(ImageResource.class.getResourceAsStream("/image/obstacle.png")));
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
