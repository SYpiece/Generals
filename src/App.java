import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Game;
import model.Player;
import resource.color.Color;
import ui.GameSceneController;
import ui.InitializingSceneController;

import java.util.Objects;
//import ui.frame.GameFrame;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("ui/main scene.fxml"));
//        loader.setControllerFactory(param -> {
//            Game.GameSetting setting = new Game.GameSetting();
//            setting.addPlayer(Player.GamePlayer.createCompetitor(new Player.PlayerInformation("", 0), Color.RED, 1));
//            setting.setMapHeight(50);
//            setting.setMapWidth(50);
//            Game game = new Game(setting);
//            return new GameSceneController(null);
//        });
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("ui/scene.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
