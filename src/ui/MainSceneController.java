package ui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    @FXML
    private Pane roomPane;
    @FXML
    private Node playButton;
    @FXML
    private Node joinButton;
    @FXML
    private Node createButton;
    @FXML
    private void onPlayButtonClicked() {
        roomPane.setVisible(true);
    }
    @FXML
    private void onRoomPaneClicked() {
        roomPane.setVisible(false);
    }
    @FXML
    private void onUnwantedEvent(Event event) {
        event.consume();
    }
    @FXML
    private void onCreateButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainSceneController.class.getResource("room scene.fxml"));
        loader.setControllerFactory(param -> new RoomSceneController(true));
        roomPane.getScene().setRoot(loader.load());
    }
    @FXML
    private void onJoinButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainSceneController.class.getResource("room scene.fxml"));
        loader.setControllerFactory(param -> new RoomSceneController(false));
        roomPane.getScene().setRoot(loader.load());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
