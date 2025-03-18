package socket.event;

import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultMessageEvent extends EventBase implements MessageEvent {
    protected Message message;

    @Override
    public Message getMessage() {
        return message;
    }

    public DefaultMessageEvent(Message message) {
        super(EventType.MessageEvent);
        this.message = message;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUnshared(message);
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        message = (Message) inputStream.readUnshared();
    }
}
