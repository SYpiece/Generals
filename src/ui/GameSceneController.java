package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import socket.GameClient;
import socket.event.GameClientAdapter;
import socket.event.GameClientEvent;
import socket.event.GamePlayerClientAdapter;
import socket.event.GamePlayerClientEvent;
import ui.control.GamePane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable {
    @FXML
    private Pane _rootPane;
    private final GamePane _gamePane;
    private final GameClient _gameClient;
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
    public GameSceneController(GameClient gameClient) {
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
