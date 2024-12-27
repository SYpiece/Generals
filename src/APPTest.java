import javafx.application.Application;
import javafx.stage.Stage;
import model.Player;
import socket.GameClient;
import socket.GameServer;
import socket.event.GameClientAdapter;
import socket.event.GameClientEvent;
import socket.event.GamePlayerClientAdapter;
import socket.event.GamePlayerClientEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class APPTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        test2();
    }
    private static void test1() throws IOException, ClassNotFoundException {
        SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName("localhost"), 44444);
        GameServer gameServer = new GameServer(socketAddress);
        gameServer.start();
        GameClient gameClient = new GameClient(socketAddress, new Player.PlayerInformation());
        gameClient.addClientListener(new GameClientAdapter() {
            @Override
            public void gameJoined(GameClientEvent event) {
                System.out.println("joined game!");
            }

            @Override
            public void gameStatusChanged(GameClientEvent event) {
                System.out.println(event.getGameStatus());
            }
        });
        gameClient.addPlayerClientListener(new GamePlayerClientAdapter() {
            @Override
            public void gameMapUpdated(GamePlayerClientEvent event) {
                System.out.println("gameMapUpdated");
            }
        });
        gameClient.start();
        gameClient.setForceState(true);
    }
    private static void test2() {
        new Application() {
            @Override
            public void start(Stage primaryStage) throws Exception {

            }
        };
        Application.launch();
    }
}
