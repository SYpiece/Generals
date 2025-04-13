package util.socket;

public interface Connection<T extends Message> extends Readable<T>, Writeable<T>, Handleable, AutoCloseable {
}
