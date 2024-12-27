package model;

import util.model.IdentityDocument;
import util.model.Modelable;

import java.io.Serializable;

public final class Message implements Serializable {
    private final IdentityDocument _senderID;
    private final String _message;
    public IdentityDocument getSenderID() {
        return _senderID;
    }
    public String message() {
        return _message;
    }
    public Message(IdentityDocument senderID, String message) {
        _senderID = senderID;
        _message = message;
//        _id = IdentityDocument.createID();
    }
//    protected Message(IdentityDocument id) {
//        _id = id;
//    }
//    protected final IdentityDocument _id;
//    @Override
//    public IdentityDocument getID() {
//        return _id;
//    }
}
