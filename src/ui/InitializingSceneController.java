package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import socket.GameClient;
import socket.GameStatus;
import socket.event.GameClientAdapter;
import socket.event.GameClientEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitializingSceneController implements Initializable {
    @FXML
    private Pane _rootPane;
    private final GameClient _gameClient;
    private final GameClientAdapter _clientAdapter = new GameClientAdapter() {
        @Override
        public void gameStatusChanged(GameClientEvent event) {
            if (event.getGameStatus() != GameStatus.Initializing) {
                _gameClient.removeClientListener(_clientAdapter);
                Platform.runLater(() -> {
                    try {
                        startGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    };
    public InitializingSceneController(GameClient gameClient) {
        _gameClient = gameClient;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _gameClient.addClientListener(_clientAdapter);
    }
    private void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(InitializingSceneController.class.getResource("game scene.fxml"));
        loader.setController(new GameScene(_gameClient));
        _rootPane.getScene().setRoot(loader.load());
    }
}
