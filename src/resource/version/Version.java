package resource.version;

import java.io.Serializable;

public class Version implements Serializable {
    private final int _gameVersion, _coreVersion;
    public int getGameVersion() {
        return _gameVersion;
    }
    public int getCoreVersion() {
        return _coreVersion;
    }
    public Version(int gameVersion, int coreVersion) {
        _gameVersion = gameVersion;
        _coreVersion = coreVersion;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version version = (Version) obj;
            return _gameVersion == version._gameVersion && _coreVersion == version._coreVersion;
        } else {
            return super.equals(obj);
        }
    }
}
