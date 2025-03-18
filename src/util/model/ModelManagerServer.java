package util.model;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ModelManagerServer implements Closeable {
    protected List<ModelManagerConnection> _connections = new LinkedList<>();
    protected Status _status;
    public List<ModelManagerConnection> getConnections() throws IOException {
        if (_status != Status.Opened) {
            throw new IOException();
        }
        return Collections.unmodifiableList(_connections);
    }
    public Status getStatus() {
        return _status;
    }
    public ModelManagerServer() {
        _status = Status.Opened;
    }
    public boolean containsConnection(ModelManagerConnection connection) throws IOException {
        if (_status != Status.Opened) {
            throw new IOException();
        }
        return _connections.contains(connection);
    }
    public void addConnection(ModelManagerConnection connection) throws IOException {
        if (_status != Status.Opened || connection.getStatus() != ModelManagerConnection.Status.Connected || _connections.contains(connection)) {
            throw new IOException();
        }
        _connections.add(connection);
    }
    public void removeConnection(ModelManagerConnection connection) throws IOException {
        if (_status != Status.Opened || !_connections.remove(connection)) {
            throw new IOException();
        }
    }
    @Override
    public void close() throws IOException {
        if (_status != Status.Opened) {
            throw new IOException();
        }
        _connections = null;
        _status = Status.Closed;
    }
    public enum Status {
        Opened, Closed
    }
}
