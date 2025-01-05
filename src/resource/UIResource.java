package resource;

import java.net.URL;

public final class UIResource extends Resource {
    private final static URL
            _gameSceneFXML = getURL("/resource/ui/game scene.fxml"),
            _initializingSceneFXML = getURL("/resource/ui/initializing scene.fxml"),
            _mainSceneFXML = getURL("/resource/ui/main scene.fxml"),
            _roomSceneFXML = getURL("/resource/ui/room scene.fxml");
    private final static String
            _mainSceneCSS = getPath("/resource/ui/main scene.css");
    public static URL getGameSceneFXML() {
        return _gameSceneFXML;
    }
    public static URL getInitializingSceneFXML() {
        return  _initializingSceneFXML;
    }
    public static URL getMainSceneFXML() {
        return _mainSceneFXML;
    }
    public static URL getRoomSceneFXML() {
        return _roomSceneFXML;
    }
    public static String getMainSceneCss() {
        return _mainSceneCSS;
    }
    private UIResource() {}
}
