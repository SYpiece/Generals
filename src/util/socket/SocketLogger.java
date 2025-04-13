package util.socket;

import java.util.logging.Level;
import java.util.logging.Logger;

class SocketLogger {
    protected static final Logger logger = Logger.getLogger(SocketLogger.class.getName());

    public static void logError(Throwable e) {
        logger.log(Level.WARNING, e.getMessage());
    }

    private SocketLogger() {}
}
