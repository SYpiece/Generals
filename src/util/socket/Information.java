package util.socket;

public interface Information extends Message {
    CommunicationType getType();

    Information getAnother();
}
