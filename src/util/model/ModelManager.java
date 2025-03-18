package util.model;

import java.io.Closeable;
import java.util.*;

public class ModelManager implements Closeable {
    protected Map<IdentityDocument, Model> _models = new HashMap<>();
//    new TreeMap<>((id1, id2) -> {
//        if (id1 == id2) {
//            return 0;
//        }
//        List<Long> longs1 = id1.getID(), longs2 = id2.getID();
//        if (longs1.size() == longs2.size()) {
//            for (Iterator<Long> it1 = longs1.iterator(), it2 = longs2.iterator(); it1.hasNext() && it2.hasNext();) {
//                long la = it1.next(), lb = it2.next();
//                if (la != lb) {
//                    int la1 = (int) (la >> 32), la2 = (int) la, lb1 = (int) (lb >> 32), lb2 = (int) lb;
//                    if (la1 != lb1) {
//                        return la1 - lb1;
//                    } else {
//                        return la2 - lb2;
//                    }
//                }
//            }
//            return 0;
//        } else {
//            return longs1.size() - longs2.size();
//        }
//    });
    protected boolean _closed = false;
    public ModelManager() {}
    public IdentityDocument addModel(Model model) {
        checkClosedStatus();
        IdentityDocument id = getID(model);
        if (id == null) {
            id = IdentityDocument.createID();
            _models.put(id, model);
        }
        return id;
    }
    public IdentityDocument addModel(IdentityDocument identityDocument, Model model) {
        checkClosedStatus();
        IdentityDocument id = getID(model);
        if (id == null) {
            if (_models.containsKey(identityDocument)) {
                id = IdentityDocument.createID();
            }
            _models.put(id, model);
        }
        return id;
    }
    public void removeID(IdentityDocument id) {
        checkClosedStatus();
        _models.remove(id);
    }
    public void removeModel(Model model) {
        checkClosedStatus();
        _models.values().removeIf(value -> value == model);
    }
    public boolean containsID(IdentityDocument id) {
        checkClosedStatus();
        return _models.containsKey(id);
    }
    public boolean containsModel(Model model) {
        checkClosedStatus();
        return _models.containsValue(model);
    }
    public IdentityDocument getID(Model model) {
        checkClosedStatus();
        for (Map.Entry<IdentityDocument, Model> entry : _models.entrySet()) {
            if (entry.getValue() == model) {
                return entry.getKey();
            }
        }
        return null;
    }
    public Model getModel(IdentityDocument id) {
        checkClosedStatus();
        return _models.get(id);
    }
    public ModelPack createModelPack(Model model) {
        checkClosedStatus();
        return new ModelPack(this, model);
    }
    public Model analyzeModelPack(ModelPack pack) {
        checkClosedStatus();

    }
    @Override
    public void close() {
        checkClosedStatus();
        _models = null;
        _closed = true;
    }
    protected void checkClosedStatus() {
        if (_closed) {
            throw new RuntimeException();
        }
    }
}
