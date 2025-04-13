package util.socket;

public interface Communication<T extends Information> extends Connection<T>, Answerable<T>, Questionable<T> {
}
