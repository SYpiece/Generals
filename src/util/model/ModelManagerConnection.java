package util.model;

import java.io.*;

public class ModelManagerConnection implements AutoCloseable {
    protected ObjectInputStream _inputStream;
    protected ObjectOutputStream _outputStream;
    protected Status _status;
    public Status getStatus() {
        return _status;
    }
    public ModelManagerConnection(InputStream inputStream, OutputStream outputStream) throws IOException {
        this();
        connect(inputStream, outputStream);
    }
    public ModelManagerConnection() {
        _status = Status.Unconnected;
    }
    public void connect(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (_status != Status.Unconnected) {
            throw new IOException();
        }
        _inputStream = new ObjectInputStream(inputStream);
        _outputStream = new ObjectOutputStream(outputStream);
        _status = Status.Connected;
    }
    public void disconnect() throws IOException {
        if (_status != Status.Connected) {
            throw new IOException();
        }
        _inputStream.close();
        _outputStream.close();
        _status = Status.Disconnected;
    }
    @Override
    public void close() throws IOException {
        if (_status == Status.Connected) {
            disconnect();
        }
    }
    public enum Status {
        Unconnected, Connected, Disconnected
    }
}
