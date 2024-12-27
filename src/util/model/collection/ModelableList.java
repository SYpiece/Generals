package util.model.collection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.List;

import util.model.IdentityDocument;
import util.model.Modelable;
import util.model.ModelsManager;

public class ModelableList implements ModelableCollection, Externalizable {
    private Object[] _array;
    private Class<?> _class;
    public ModelableList() {}
    @Override
    public ModelableCollectionType collectionType() {
        return ModelableCollectionType.List;
    }
    @Override
    public void setValue(Object object, ModelsManager modelsManager) {
        if (!(object instanceof List)) {
            throw new IllegalArgumentException("object is not an List");
        }
        _class = object.getClass();
        _array = new Object[Array.getLength(object)];
        for (int i = 0; i < Array.getLength(object); i++) {
            Object value = Array.get(object, i);
            if (value instanceof Modelable) {
                modelsManager.includeModel((Modelable) value);
                _array[i] = ((Modelable) value).getID();
            } else if (value instanceof IdentityDocument) {
                _array[i] = null;
            } else {
                _array[i] = value;
            }
        }
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object getValue(ModelsManager modelsManager) {
        List list;
        try {
            list = (List)_class.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < _array.length; i++) {
            if (_array[i] instanceof IdentityDocument) {
                list.add(modelsManager.getModel((IdentityDocument) _array[i]));
            } else if (_array[i] instanceof ModelableCollection) {
                list.add(((ModelableCollection) _array[i]).getValue(modelsManager));
            } else {
                list.add(_array[i]);
            }
        }
        return list;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(_array);
        out.writeObject(_class);
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        _array = (Object[]) in.readObject();
        _class = (Class<?>) in.readObject();
    }
    @Override
    public Class<?> getComponentType() {
        return _class;
    }
}
