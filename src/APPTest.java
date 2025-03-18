import javafx.application.Application;
import javafx.stage.Stage;
import model.DefaultPlayer;
import socket.DefaultGameClient;
import socket.DefaultServer;
import socket.even.GameClientAdapter;
import socket.even.GameClientEvent;
import socket.even.GamePlayerClientAdapter;
import socket.even.GamePlayerClientEvent;

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
        DefaultServer gameServer = new DefaultServer(socketAddress);
        gameServer.start();
        DefaultGameClient gameClient = new DefaultGameClient(socketAddress, new DefaultPlayer.PlayerInformation());
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
