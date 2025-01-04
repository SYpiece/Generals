package resource;

import resource.version.Version;

public final class VersionResource extends Resource {
    private static final Version _version = new Version(readString("/resource/version/game version"), readString("/resource/version/core version"));
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
