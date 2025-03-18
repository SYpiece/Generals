package socket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Handleable {
    void handle() throws IOException;
}
