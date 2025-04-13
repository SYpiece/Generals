package util.socket;

public enum CommunicationType {
    Answer(0),
    Query(1);

    private final int type;

    CommunicationType(int type) {
        this.type = type;
    }

    public int toInteger() {
        return type;
    }

    public static int toInteger(CommunicationType type) {
        return type.toInteger();
    }

    public static CommunicationType fromInteger(int value) {
        switch (value) {
            case 0: return Answer;
            case 1: return Query;
            default: throw new IllegalArgumentException();
        }
    }
}
