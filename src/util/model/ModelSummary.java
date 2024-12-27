package util.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import util.model.collection.ModelableCollections;

public class ModelSummary implements Serializable {
    private static final long serialVersionUID = -552502009362829104L;
	private ModelProperty[] _properties;
    private IdentityDocument _id;
    private Class<? extends Modelable> _class;
    public ModelProperty[] modelProperties() {
        return _properties.clone();
    }
    public IdentityDocument id() {
        return _id;
    }
    public Class<? extends Modelable> modelClass() {
        return _class;
    }
    private ModelProperty[] getModelProperties(ModelsManager modelsManager) {
        Modelable model = modelsManager.getModel(_id);
        if (model == null) {
            return null;
        }
        if (model instanceof ExternModelable) {
            return ((ExternModelable) model).getModelProperties(modelsManager);
        } else {
            List<ModelProperty> properties = new LinkedList<>();
            for (Class<?> modelClass = model.getClass(); modelClass != Object.class; modelClass = modelClass.getSuperclass()) {
                for (Field field : modelClass.getDeclaredFields()) {
                    if (Modifier.isTransient(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object object = null;
                    try {
                        object = field.get(model);
                        if (object != null) {
                            Class<?> objClass = object.getClass();
                            if (objClass == IdentityDocument.class) {
                                continue;
                            } else if (object instanceof Modelable) {
                                Modelable m = ((Modelable)object);
                                if (modelsManager.includeModel(m)) {
                                    object = m.getID();
                                } else {
                                    object = null;
                                }
                            } else if (ModelableCollections.isModelableCollection(object)) {
                                object = ModelableCollections.modelableCollection(object, modelsManager);
                            }
                        }
                    } catch (Exception e) {}
                    properties.add(modelsManager.createProperty(model, field.getName(), object));
                }
            }
            return properties.toArray(new ModelProperty[properties.size()]);
        }
    }
    public Modelable setModelProperties(ModelsManager modelsManager) {
        Modelable model = modelsManager.getModel(_id);
        if (model == null) {
            return null;
        }
        if (model instanceof ExternModelable) {
            ((ExternModelable) model).setModelProperties(modelsManager, _properties);
        } else {
            for (ModelProperty property : _properties) {
                property.setModelProperty(model, modelsManager);
            }
        }
        return model;
    }
    ModelSummary(Modelable model, ModelsManager modelsManager) {
        _properties = getModelProperties(modelsManager);
        _id = model.getID();
        _class = model.getClass();
    }
}