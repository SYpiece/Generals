package resource;

import jdk.nashorn.internal.parser.JSONParser;
import resource.version.Version;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class VersionResource {
    protected static final Version _version;
    static {
        _version = new Version(0, 0);
    }
    public static Version getVersion() {
        return _version;
    }
    private VersionResource() {}
//    private static String readString(String jarPath) {
//        return readString(VersionResource.class.getResource(jarPath));
//    }
//    private static String readString(URL url) {
//        try {
//            InputStream inputStream = url.openStream();
//            byte[] bytes = new byte[inputStream.available()];
//            inputStream.read(bytes);
//            return new String(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Unkown";
//        }
//    }
}
