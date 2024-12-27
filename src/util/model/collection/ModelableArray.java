package util.model.collection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;

import util.model.IdentityDocument;
import util.model.Modelable;
import util.model.ModelsManager;

public class ModelableArray implements ModelableCollection, Externalizable {
    private Object[] _array;
    private Class<?> _class;
    public ModelableArray() {}
    @Override
    public ModelableCollectionType collectionType() {
        return ModelableCollectionType.Array;
    }
    @Override
    public void setValue(Object object, ModelsManager modelsManager) {
        _class = object.getClass();
        if (!_class.isArray()) {
            _class = null;
            throw new IllegalArgumentException("object is not an array");
        }
        _array = new Object[Array.getLength(object)];
        for (int i = 0; i < Array.getLength(object); i++) {
            Object value = Array.get(object, i);
            if (value instanceof Modelable) {
                modelsManager.includeModel((Modelable) value);
                _array[i] = ((Modelable) value).getID();
            } else if (value instanceof IdentityDocument) {
                _array[i] = null;
            } else if (ModelableCollections.isModelableCollection(value)) {
                _array[i] = ModelableCollections.modelableCollection(value, modelsManager);
            } else {
                _array[i] = value;
            }
        }
    }
    @Override
    public Object getValue(ModelsManager modelsManager) {
        Object array = Array.newInstance(_class, _array.length);
        for (int i = 0; i < _array.length; i++) {
            if (_array[i] instanceof IdentityDocument) {
                Array.set(array, i, modelsManager.getModel((IdentityDocument) _array[i]));
            } else if (_array[i] instanceof ModelableCollection) {
                Array.set(array, i, ((ModelableCollection) _array[i]).getValue(modelsManager));
            } else {
                Array.set(array, i, _array[i]);
            }
        }
        return array;
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
