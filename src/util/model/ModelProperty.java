package util.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import util.model.collection.ModelableCollection;
import util.model.collection.ModelableCollections;

public class ModelProperty implements Serializable {
    private static final long serialVersionUID = -2968349349190197302L;
//	private String _key;
//    private Object _value;
//    public String key() {
//        return _key;
//    }
//    public Object originalValue() {
//        return _value;
//    }
//    public Object modelValue(ModelsManager modelsManager) {
//        return demodellingProperty(_value, modelsManager);
//    }
//    public boolean setModelProperty(Modelable model, ModelsManager modelsManager) {
//        if (!modelsManager.includeModel(model)) {
//            return false;
//        }
//        if (model instanceof ExternModelable) {
//            ((ExternModelable) model).setModelProperty(modelsManager, this);
//        } else {
//            Class<? extends Modelable> modelClass = model.getClass();
//            try {
//                Field field = modelClass.getField(_key);
//                if (!Modifier.isTransient(field.getModifiers())) {
//                    field.setAccessible(true);
//                    field.set(model, demodellingProperty(_value, modelsManager));
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return true;
//    }
//    private Object getModelProperty(Modelable model, String key, ModelsManager modelsManager) {
//        if (!modelsManager.includeModel(model)) {
//            return null;
//        }
//        if (model instanceof ExternModelable) {
//            return ((ExternModelable) model).getModelProperty(modelsManager, key);
//        } else {
//            try {
//                Field field = model.getClass().getField(key);
//                if (Modifier.isTransient(field.getModifiers())) {
//                    return null;
//                }
//                field.setAccessible(true);
//                return field.get(model);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//    private Object modellingProperty(Object model, ModelsManager modelsManager) {
//        if (model instanceof Modelable) {
//            if (modelsManager.includeModel((Modelable) model)) {
//                return ((Modelable) model).getID();
//            } else {
//                return null;
//            }
//        } else if (model instanceof IdentityDocument) {
//            return null;
//        } else if (ModelableCollections.isModelableCollection(model)) {
//            return ModelableCollections.modelableCollection(model, modelsManager);
//        } else {
//            return model;
//        }
//    }
//    private Object demodellingProperty(Object value, ModelsManager modelsManager) {
//        if (value instanceof IdentityDocument) {
//            return modelsManager.getModel((IdentityDocument) value);
//        } else if (value instanceof ModelableCollection) {
//            return ((ModelableCollection) value).getValue(modelsManager);
//        } else {
//            return value;
//        }
//    }
//    ModelProperty(Modelable model, String key, ModelsManager modelsManager) {
//        _key = key;
//        _value = modellingProperty(getModelProperty(model, key, modelsManager), modelsManager);
//    }
//    ModelProperty(String key, Object value, ModelsManager modelsManager) {
//        _key = key;
//        _value = modellingProperty(value, modelsManager);
//    }
    protected final Type _type;
    protected final Object _object;
    public Type getType() {
        return _type;
    }
    public Object getObject() {
        return _object;
    }
    ModelProperty(Type type, Object object) {
        _type = type;
        _object = object;
    }
    public enum Type {
        Object, Pack, Model
    }
}
