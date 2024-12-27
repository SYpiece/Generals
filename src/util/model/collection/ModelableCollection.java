package util.model.collection;

import java.io.Serializable;

import util.model.ModelsManager;

public interface ModelableCollection extends Serializable {
	ModelableCollectionType collectionType();
    void setValue(Object object, ModelsManager modelsManager);
    Object getValue(ModelsManager modelsManager);
    Class<?> getComponentType();
}
