package resource;

import javafx.scene.image.Image;
import model.ImageType;

public final class ImageResource extends Resource {
    private static final Image cityImage = readImage("/image/city.png");
    private static final Image crownImage = readImage("/image/crown.png");
    private static final Image mountainImage = readImage("/image/mountain.png");
    private static final Image obstacleImage = readImage("/image/obstacle.png");
    public static Image getBlockImage(ImageType imageType) {
        switch (imageType) {
            case City:
                return cityImage;
            case Crown:
                return crownImage;
            case Mountain:
                return mountainImage;
            case Obstacle:
                return obstacleImage;
            default:
                return null;
        }
    }
    private ImageResource() {}
}
