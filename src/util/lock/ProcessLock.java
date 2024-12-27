package util.lock;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProcessLock implements Closeable {
    private FileOutputStream _fileOutputStream;
    public ProcessLock(File file) throws FileNotFoundException {
        _fileOutputStream = new FileOutputStream(file);
    }
    public void lock() throws IOException {
        _fileOutputStream.getChannel().lock();
    }
    @Override
    public void close() throws IOException {
        _fileOutputStream.close();
    }
}
