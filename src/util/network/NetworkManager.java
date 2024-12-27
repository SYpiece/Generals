package util.network;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class NetworkManager {
    private static final int port = 2300;
    private static List<MulticastSocket> multicastSockets = new LinkedList<>();
    private static MulticastSocket multicastSocket;
    private static Socket socket;
    private static List<ServerSocket> serverSockets = new LinkedList<>();
    private static final byte[] id = new byte[256];
    private static final byte[] introduction = new byte[512];
    static {
        Random random = new Random();
        random.nextBytes(id);
        MessageDigest messageDigest = getMessageDigest();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(id);
        byte[] sha = messageDigest.digest();
        int j = 0;
        for (int i = 0; i < sha.length; i++, j++) {
            introduction[j] = sha[i];
        }
        for (int i = 0; i < id.length; i++, j++) {
            introduction[j] = id[i];
        }
    }
    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void startup() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces == null) {
            return;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress inetAddress = ias.nextElement();
                try {
                    InetAddress ia = null;
                    if (inetAddress instanceof Inet4Address) {
                        ia = Inet4Address.getByName("224.0.0.1");
                    } else {
                        ia = Inet6Address.getByName("FF01::1");
                    }
                    MulticastSocket multicastSocket = new MulticastSocket(new InetSocketAddress(inetAddress, port));
                    multicastSocket.joinGroup(new InetSocketAddress(ia, port), networkInterface);
                    multicastSocket.setTimeToLive(255);
                    multicastSockets.add(multicastSocket);
                } catch (Exception e) {}
                try {
                    ServerSocket serverSocket = new ServerSocket(port, 0, inetAddress);
                    serverSockets.add(serverSocket);
                } catch (Exception e) {}
            }
        }
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(Inet4Address.getByName("224.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void find() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (MulticastSocket multicastSocket : multicastSockets) {
                        DatagramPacket datagramPacket = new DatagramPacket(introduction, port, multicastSocket.getRemoteSocketAddress());
                        try {
                            multicastSocket.send(datagramPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    byte[] buffer = new byte[512];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    try {
                        multicastSocket.receive(datagramPacket);
                        MessageDigest messageDigest = getMessageDigest();
                        messageDigest.update(buffer, 256, 256);
//                        if (Arrays.compare(messageDigest.digest(), Arrays.copyOfRange(buffer, 0, 256)) == 0 && Arrays.compare(Arrays.copyOfRange(buffer, 256, 512), id) > 0) {
//
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
