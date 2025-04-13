package util.socket;

public interface Callable<T, A> {
    T call(A argument);
}
