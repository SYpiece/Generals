package util.model.collection;

import java.util.List;

import util.model.ModelsManager;

public class ModelableCollections {
    private ModelableCollections() {}
    public static boolean isModelableCollection(Object object) {
        return object.getClass().isArray() || object instanceof List;
    }
    public static ModelableArray modelableArray(Object array, ModelsManager modelsManager) {
        ModelableArray modelableArray = new ModelableArray();
        modelableArray.setValue(array, modelsManager);
        return modelableArray;
    }
    @SuppressWarnings("rawtypes")
    public static ModelableList modelableList(List list, ModelsManager modelsManager) {
        ModelableList modelableList = new ModelableList();
        modelableList.setValue(list, modelsManager);
        return modelableList;
    }
    @SuppressWarnings("rawtypes")
    public static ModelableCollection modelableCollection(Object collection, ModelsManager modelsManager) {
        ModelableCollection modelableCollection = null;
        if (collection.getClass().isArray()) {
            modelableCollection = modelableArray(collection, modelsManager);
        } else if (collection instanceof List) {
            modelableCollection = modelableList((List) collection, modelsManager);
        }
        return modelableCollection;
    }
}
