package util.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class IdentityDocument implements Serializable {
    private static final long serialVersionUID = -1418021736556074685L;
    private final List<Long> _id;
    public List<Long> getID() {
        return _id;
    }
    IdentityDocument(long... id) {
        List<Long> longs = new ArrayList<>(id.length);
        for (long l : id) {
            longs.add(l);
        }
        _id = Collections.unmodifiableList(longs);
    }
    IdentityDocument(List<Long> longs) {
        _id = Collections.unmodifiableList(new ArrayList<>(longs));
    }
    @Override
    public boolean equals(Object object) {
        if (object instanceof IdentityDocument) {
            IdentityDocument other = (IdentityDocument) object;
            if (_id.size() == other._id.size()) {
                for (Iterator<Long> it1 = _id.iterator(), it2 = other._id.iterator(); it1.hasNext() && it2.hasNext();) {
                    if (!it1.next().equals(it2.next())) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
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
