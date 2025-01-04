package resource;

import jdk.nashorn.internal.parser.JSONParser;
import resource.version.Version;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class VersionResource extends Resource {
    private static final Version _version;
    static {
        _version = new Version(readString("/resource/version/game version"), readString("/resource/version/core version"));
    }
    public static Version getVersion() {
        return _version;
    }
    public static String getGameVersion() {
        return _version.getGameVersion();
    }
    public static String getCoreVersion() {
        return _version.getCoreVersion();
    }
    private VersionResource() {}
}
