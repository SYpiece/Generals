package resource;

import model.User;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class FileResource extends Resource {
    private static final File _gameFile, _userFile;
    static {
        _gameFile = new File(".generals");
        if (!_gameFile.exists()) {
            initializeGameFile();
        }
        _userFile = new File(_gameFile, "player.bin");
        if (!_userFile.exists()) {
            initializeUserFile();
        }
    }
    public static File getGameFile() {
        return _gameFile;
    }
    public static File getUserFile() {
        return _userFile;
    }
    private static void initializeGameFile() {
        if (_gameFile.exists()) {
            _gameFile.delete();
        }
        _gameFile.mkdirs();
    }
    private static void initializeUserFile() {
        if (_userFile.exists()) {
            _userFile.delete();
        }
        try {
            _userFile.createNewFile();
            try (
                    OutputStream outputStream = Files.newOutputStream(_userFile.toPath());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(new User());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private FileResource() {}
}
