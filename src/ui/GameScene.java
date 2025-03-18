package ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import resource.UIResource;
import socket.DefaultGameClient;
import socket.even.GameClientAdapter;
import socket.even.GameClientEvent;
import socket.even.GamePlayerClientAdapter;
import socket.even.GamePlayerClientEvent;
import ui.control.GamePane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameScene extends Scene implements Initializable {
    public GameScene() {
        super(new AnchorPane());
        FXMLLoader fxmlLoader = new FXMLLoader(UIResource.getGameSceneFXML());
        fxmlLoader.setRoot(getRoot());
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final Pane _rootPane = (Pane) getRoot();
    private final GamePane _gamePane;
    private final DefaultGameClient _gameClient;
    private final GamePlayerClientAdapter _playerClientAdapter = new GamePlayerClientAdapter() {
        @Override
        public void gameMapUpdated(GamePlayerClientEvent event) {
            Platform.runLater(_gamePane::repaint);
        }
    };
    private final GameClientAdapter _clientAdapter = new GameClientAdapter() {
        @Override
        public void gameExited(GameClientEvent event) {
            _gameClient.removeClientListener(_clientAdapter);
            _gameClient.removePlayerClientListener(_playerClientAdapter);
        }
    };
    public GameScene(DefaultGameClient gameClient) {
        super(new AnchorPane());
        _gameClient = gameClient;
        _gamePane = new GamePane(gameClient);
        _gamePane.setTableWidth(600);
        _gamePane.setTableHeight(600);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _rootPane.getChildren().add(0, _gamePane);
        _gamePane.widthProperty().bind(_rootPane.widthProperty());
        _gamePane.heightProperty().bind(_rootPane.heightProperty());
        _gameClient.addClientListener(_clientAdapter);
        _gameClient.addPlayerClientListener(_playerClientAdapter);
    }
}
