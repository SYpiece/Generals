package util.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ModelSummary implements Serializable {
    private static final long serialVersionUID = -552502009362829104L;
//	private ModelProperty[] _properties;
//    private IdentityDocument _id;
//    private Class<? extends Modelable> _class;
//    public ModelProperty[] modelProperties() {
//        return _properties.clone();
//    }
//    public IdentityDocument id() {
//        return _id;
//    }
//    public Class<? extends Modelable> modelClass() {
//        return _class;
//    }
//    private ModelProperty[] getModelProperties(ModelsManager modelsManager) {
//        Modelable model = modelsManager.getModel(_id);
//        if (model == null) {
//            return null;
//        }
//        if (model instanceof ExternModelable) {
//            return ((ExternModelable) model).getModelProperties(modelsManager);
//        } else {
//            List<ModelProperty> properties = new LinkedList<>();
//            for (Class<?> modelClass = model.getClass(); modelClass != Object.class; modelClass = modelClass.getSuperclass()) {
//                for (Field field : modelClass.getDeclaredFields()) {
//                    if (Modifier.isTransient(field.getModifiers())) {
//                        continue;
//                    }
//                    field.setAccessible(true);
//                    Object object = null;
//                    try {
//                        object = field.get(model);
//                        if (object != null) {
//                            Class<?> objClass = object.getClass();
//                            if (objClass == IdentityDocument.class) {
//                                continue;
//                            } else if (object instanceof Modelable) {
//                                Modelable m = ((Modelable)object);
//                                if (modelsManager.includeModel(m)) {
//                                    object = m.getID();
//                                } else {
//                                    object = null;
//                                }
//                            } else if (ModelableCollections.isModelableCollection(object)) {
//                                object = ModelableCollections.modelableCollection(object, modelsManager);
//                            }
//                        }
//                    } catch (Exception e) {}
//                    properties.add(modelsManager.createProperty(model, field.getName(), object));
//                }
//            }
//            return properties.toArray(new ModelProperty[properties.size()]);
//        }
//    }
//    public Modelable setModelProperties(ModelsManager modelsManager) {
//        Modelable model = modelsManager.getModel(_id);
//        if (model == null) {
//            return null;
//        }
//        if (model instanceof ExternModelable) {
//            ((ExternModelable) model).setModelProperties(modelsManager, _properties);
//        } else {
//            for (ModelProperty property : _properties) {
//                property.setModelProperty(model, modelsManager);
//            }
//        }
//        return model;
//    }
//    ModelSummary(Modelable model, ModelsManager modelsManager) {
//        _properties = getModelProperties(modelsManager);
//        _id = model.getID();
//        _class = model.getClass();
//    }

    protected final IdentityDocument _id = IdentityDocument.createID();
    public IdentityDocument getID() {
        return _id;
    }
    protected Map<String, ModelProperty> _properties = new HashMap<>();
    ModelSummary(ModelManager manager, ModelPack pack, Model model) {
        Class<? extends Model> clazz = model.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object object = field.get(model);
                if (object instanceof Model) {
                    Model m = (Model) object;
                    IdentityDocument id = manager.getID(m);
                    if (id == null) {
                        _properties.put(field.getName(), new ModelProperty(ModelProperty.Type.Pack, pack.addModel(manager, m).getID()));
                    } else {
                        _properties.put(field.getName(), new ModelProperty(ModelProperty.Type.Model, id));
                    }
                } else {
                    _properties.put(field.getName(), new ModelProperty(ModelProperty.Type.Object, object));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}