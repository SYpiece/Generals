package ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resource.UIResource;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScene extends Scene implements Initializable {
    public MainScene() {
        super(new AnchorPane());
        FXMLLoader fxmlLoader = new FXMLLoader(UIResource.getMainSceneFXML());
        fxmlLoader.setRoot(getRoot());
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getStylesheets().add(UIResource.getMainSceneCss());
    }
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
//        FXMLLoader loader = new FXMLLoader(MainScene.class.getResource("/ui/room scene.fxml"));
//        loader.setControllerFactory(param -> new RoomSceneController(true));
//        roomPane.getScene().setRoot(loader.load());
        ((Stage) getWindow()).setScene(new RoomScene(true));
    }
    @FXML
    private void onJoinButtonClicked() throws IOException {
//        FXMLLoader loader = new FXMLLoader(MainScene.class.getResource("/ui/room scene.fxml"));
//        loader.setControllerFactory(param -> new RoomSceneController(false));
//        roomPane.getScene().setRoot(loader.load());
        ((Stage) getWindow()).setScene(new RoomScene(false));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
