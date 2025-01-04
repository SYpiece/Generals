package resource;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class Resource {
    protected Resource() {}
    protected static URL getURL(String jarPath) {
        return Objects.requireNonNull(Resource.class.getResource(jarPath));
    }
    protected static String getPath(String jarPath) {
        return getURL(jarPath).toExternalForm();
    }
    protected static String readString(String jarPath) {
        return readString(getURL(jarPath));
    }
    protected static String readString(URL url) {
        try (InputStream inputStream = url.openStream()) {
            byte[] bytes = new byte[inputStream.available()];
            int result = inputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected static Image readImage(String jarPath) {
        return readImage(getURL(jarPath));
    }
    protected static Image readImage(URL url) {
        try {
            return new Image(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected static Object readObject(URL url) {
        try (
                InputStream inputStream = url.openStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    protected static Object readObject(File file) {
        try (
                InputStream inputStream = Files.newInputStream(file.toPath());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
