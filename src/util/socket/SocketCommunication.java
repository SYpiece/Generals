package util.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketCommunication extends CommunicationBase<Information> {
    protected final Socket socket;
    protected final InputStream inputStream;
    protected final ObjectInputStream objectInputStream;
    protected final ObjectOutputStream objectOutputStream;

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    SocketCommunication(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    protected int doAvailable() throws IOException {
        return inputStream.available();
    }

    @Override
    protected Information doReceive() throws IOException {
        try {
            Object object = objectInputStream.readUnshared();
            if (object instanceof Information) {
                return (Information) object;
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            throw new IOException();
        }
    }

    @Override
    protected void doSend(Information value) throws IOException {
        objectOutputStream.writeUnshared(value);
        objectOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
