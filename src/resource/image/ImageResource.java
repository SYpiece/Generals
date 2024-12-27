package resource.image;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public final class ImageResource {
    public final static Image emptyImage = null;
    public final static Image landImage = emptyImage;
    public final static Image cityImage = readImage("/resource/image/city.png");
    public final static Image crownImage = readImage("/resource/image/crown.png");
    public final static Image mountainImage = readImage("/resource/image/mountain.png");
    public final static Image obstacleImage = readImage("/resource/image/obstacle.png");
    private ImageResource() {}
    private static Image readImage(String jarPath) {
        return readImage(ImageResource.class.getResource(jarPath));
    }
    private static Image readImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
