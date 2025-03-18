package util.model;

import java.io.Closeable;
import java.io.IOException;

public class ModelManagerClient implements Closeable {
    protected ModelManagerConnection _connection;
    protected Status _status;
    public ModelManagerConnection getConnection() throws IOException {
        if (_status != Status.Connected) {
            throw new IOException();
        }
        return _connection;
    }
    public Status getStatus() {
        return _status;
    }
    public ModelManagerClient(ModelManagerConnection connection) throws IOException {
        this();
        connect(connection);
    }
    public ModelManagerClient() {
        _status = Status.Unconnected;
    }
    public void connect(ModelManagerConnection connection) throws IOException {
        if (_status != Status.Unconnected || connection.getStatus() != ModelManagerConnection.Status.Connected) {
            throw new IOException();
        }
        _connection = connection;
        _status = Status.Connected;
    }
    @Override
    public void close() throws IOException {
        if (_status != Status.Connected) {
            throw new IOException();
        }
        _connection = null;
        _status = Status.Disconnected;
    }
    public enum Status {
        Unconnected, Connected, Disconnected
    }
}
