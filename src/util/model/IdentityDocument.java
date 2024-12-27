package util.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public final class IdentityDocument implements Serializable {
    private static final long serialVersionUID = -1418021736556074685L;
	private final long[] _id;
    IdentityDocument(long... id) {
        _id = id;
    }
    public Object get() {
        return _id;
    }
    @Override
    public boolean equals(Object object) {
        if (object instanceof IdentityDocument) {
            return Arrays.equals(_id, ((IdentityDocument) object)._id);
        } else {
            return super.equals(object);
        }
    }
    private static final Random random = new Random();
    public static IdentityDocument createID() {
        return new IdentityDocument(System.currentTimeMillis(), random.nextLong(), random.nextLong(), random.nextLong());
    }
    public static IdentityDocument createID(long... longs) {
        return new IdentityDocument(longs);
    }
}
