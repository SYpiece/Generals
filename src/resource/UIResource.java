package resource;

import java.net.URL;

public final class UIResource extends Resource {
    private final static URL
            _gameSceneFxml = getURL("/resource/ui/game scene.fxml"),
            _mainSceneFxml = getURL("/resource/ui/main scene.fxml");
    private final static String
            _mainSceneCss = getPath("/resource/ui/main scene.css");
    public static URL getGameSceneFxml() {
        return _gameSceneFxml;
    }
    public static URL getMainSceneFxml() {
        return _mainSceneFxml;
    }
    public static String getMainSceneCss() {
        return _mainSceneCss;
    }
    private UIResource() {}
}
