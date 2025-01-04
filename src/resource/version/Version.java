package resource.version;

import java.io.Serializable;
import java.util.Objects;

public class Version implements Serializable {
    private final String _gameVersion, _coreVersion;
    public String getGameVersion() {
        return _gameVersion;
    }
    public String getCoreVersion() {
        return _coreVersion;
    }
    public Version(String gameVersion, String coreVersion) {
        _gameVersion = gameVersion;
        _coreVersion = coreVersion;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version version = (Version) obj;
            return _gameVersion.equals(version._gameVersion) && _coreVersion.equals(version._coreVersion);
        } else {
            return super.equals(obj);
        }
    }
}
