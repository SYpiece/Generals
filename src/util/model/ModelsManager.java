package util.model;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ModelsManager {
    private final List<Modelable> _models;
    public List<Modelable> getModels() {
        return Collections.unmodifiableList(_models);
    }
    public Modelable getModel(IdentityDocument id) {
        for (Modelable model : _models) {
            if (model.getID().equals(id)) {
                return model;
            }
        }
        return null;
    }
    public ModelsManager() {
        _models = new LinkedList<>();
    }
    public Modelable includeModel(ModelSummary summary) {
        IdentityDocument id = summary.id();
        Modelable model = getModel(id);
        if (model == null) {
            if (_modelsListener != null) {
                model = _modelsListener.modelNotManaged(new ModelsManageEvent(id));
                manageModel(model);
            }
        }
        return model;
    }
    public Modelable includeModel(IdentityDocument id) {
        Modelable model = getModel(id);
        if (model == null) {
            if (_modelsListener != null) {
                model = _modelsListener.modelNotManaged(new ModelsManageEvent(id));
                manageModel(model);
            }
        }
        return model;
    }
    public boolean includeModel(Modelable model) {
        if (model == null) {
            return false;
        }
        IdentityDocument id = model.getID();
        if (getModel(id) == null) {
            return false;
        } else {
            if (_modelsListener != null) {
                _modelsListener.modelNotManaged(new ModelsManageEvent(model));
                manageModel(model);
            }
            return true;
        }
    }
    /**
     * 管理模型
     * @param model
     */
    public boolean manageModel(Modelable model) {
        if (model == null) {
            return false;
        }
        if (includeModel(model)) {
            return false;
        } else {
            _models.add(model);
            return true;
        }
    }
    /**
     * 管理多个模型
     * @param models
     */
    public boolean[] manageModels(Modelable[] models) {
        boolean[] bools = new boolean[models.length];
        Arrays.fill(bools, false);
        for (int i = 0; i < models.length; i++) {
            bools[i] = manageModel(models[i]);
        }
        return bools;
    }
    public boolean releaseModel(Modelable model) {
        return _models.remove(model);
    }
    public Modelable releaseModelWithID(IdentityDocument id) {
        Modelable model = getModel(id);
        releaseModel(model);
        return model;
    }
    public boolean[] releaseModels(Modelable[] models) {
        boolean[] bools = new boolean[models.length];
        Arrays.fill(bools, false);
        for (int i = 0; i < models.length; i++) {
            bools[i] = releaseModel(models[i]);
        }
        return bools;
    }
    public boolean[] releaseModels(List<Modelable> models) {
        boolean[] bools = new boolean[models.size()];
        Arrays.fill(bools, false);
        for (int i = 0; i < models.size(); i++) {
            bools[i] = releaseModel(models.get(i));
        }
        return bools;
    }
    public Modelable[] releaseModelsWithIDs(IdentityDocument[] ids) {
        Modelable[] models = new Modelable[ids.length];
        for (int i = 0; i < ids.length; i++) {
            models[i] = releaseModelWithID(ids[i]);
        }
        return models;
    }
    public Modelable[] releaseModelsWithIDs(List<IdentityDocument> ids) {
        Modelable[] models = new Modelable[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            models[i] = releaseModelWithID(ids.get(i));
        }
        return models;
    }
    public Modelable[] releaseAllModels() {
        Modelable[] models = getModels().toArray(new Modelable[_models.size()]);
        _models.clear();
        return models;
    }
    /**
     * 用模型摘要修改本地模型
     * @param summary
     * @return 修改的模型
     */
    public Modelable changeModel(ModelSummary summary) {
        return summary.setModelProperties(this);
    }
    public Modelable[] changeModels(ModelSummary[] summaries) {
        Modelable[] modelables = new Modelable[summaries.length];
        for (int i = 0; i < summaries.length; i++) {
            modelables[i] = changeModel(summaries[i]);
        }
        return modelables;
    }
    public boolean changeModelProperty(Modelable model, ModelProperty property) {
        return property.setModelProperty(model, this);
    }
    public Modelable changeModelProperty(IdentityDocument id, ModelProperty property) {
        Modelable model = includeModel(id);
        property.setModelProperty(model, this);
        return model;
    }
    public Modelable createModel(ModelSummary summary) {
        Class<? extends Modelable> modelClass = summary.modelClass();
        try {
            Constructor<? extends Modelable> constructor = modelClass.getDeclaredConstructor(IdentityDocument.class);
            constructor.setAccessible(true);
            Modelable model = constructor.newInstance();
            if (_modelsListener != null) {
                _modelsListener.modelCreated(new ModelsManageEvent(model));
            }
            manageModel(model);
            if (_modelsListener != null) {
                _modelsListener.modelManaged(new ModelsManageEvent(model));
            }
            summary.setModelProperties(this);
            if (_modelsListener != null) {
                _modelsListener.modelChanged(new ModelsManageEvent(model));
            }
            return model;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    public Modelable[] createModels(ModelSummary[] summaries) {
        Modelable[] models = new Modelable[summaries.length];
        for (int i = 0; i < summaries.length; i++) {
            Class<? extends Modelable> modelClass = summaries[i].modelClass();
            try {
                Constructor<? extends Modelable> constructor = modelClass.getDeclaredConstructor(IdentityDocument.class);
                constructor.setAccessible(true);
                Modelable model = constructor.newInstance();
                if (_modelsListener != null) {
                    _modelsListener.modelCreated(new ModelsManageEvent(model));
                }
                manageModel(model);
                if (_modelsListener != null) {
                    _modelsListener.modelManaged(new ModelsManageEvent(model));
                }
                models[i] = model;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        for (int i = 0; i < summaries.length; i++) {
            summaries[i].setModelProperties(this);
            if (_modelsListener != null) {
                _modelsListener.modelChanged(new ModelsManageEvent(models[i]));
            }
        }
        return models;
    }
    public Modelable[] createModels(List<ModelSummary> summaries) {
        Modelable[] models = new Modelable[summaries.size()];
        for (int i = 0; i < summaries.size(); i++) {
            Class<? extends Modelable> modelClass = summaries.get(i).modelClass();
            try {
                Constructor<? extends Modelable> constructor = modelClass.getDeclaredConstructor(IdentityDocument.class);
                constructor.setAccessible(true);
                Modelable model = constructor.newInstance();
                if (_modelsListener != null) {
                    _modelsListener.modelCreated(new ModelsManageEvent(model));
                }
                manageModel(model);
                if (_modelsListener != null) {
                    _modelsListener.modelManaged(new ModelsManageEvent(model));
                }
                models[i] = model;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        for (int i = 0; i < summaries.size(); i++) {
            summaries.get(i).setModelProperties(this);
            if (_modelsListener != null) {
                _modelsListener.modelChanged(new ModelsManageEvent(models[i]));
            }
        }
        return models;
    }
    public ModelSummary createSummary(Modelable model) {
        if (includeModel(model)) {
            return new ModelSummary(model, this);
        } else {
            return null;
        }
    }
    public ModelSummary[] createSummaries(Modelable[] models) {
        ModelSummary[] summaries = new ModelSummary[models.length];
        for (int i = 0; i < models.length; i++) {
            summaries[i] = createSummary(models[i]);
        }
        return summaries;
    }
    public ModelSummary[] createSummaries(List<Modelable> models) {
        ModelSummary[] summaries = new ModelSummary[models.size()];
        for (int i = 0; i < models.size(); i++) {
            summaries[i] = createSummary(models.get(i));
        }
        return summaries;
    }
    public ModelSummary[] createAllSummaries() {
        return createSummaries(_models);
    }
    public ModelProperty getProperty(Modelable model, String key) {
        if (includeModel(model)) {
            return new ModelProperty(model, key, this);
        } else {
            return null;
        }
    }
    public ModelProperty createProperty(Modelable model, String key, Object value) {
        if (includeModel(model)) {
            return new ModelProperty(key, value, this);
        } else {
            return null;
        }
    }
    public ModelProperty[] getProperties(Modelable model, String[] key) {
        ModelProperty[] modelProperties = new ModelProperty[key.length];
        for (int i = 0; i < key.length; i++) {
            modelProperties[i] = getProperty(model, key[i]);
        }
        return modelProperties;
    }
    public ModelProperty[] createProperties(Modelable model, String[] key, Object[] value) {
        ModelProperty[] modelProperties = new ModelProperty[key.length];
        for (int i = 0; i < key.length; i++) {
            modelProperties[i] = createProperty(model, key[i], value[i]);
        }
        return modelProperties;
    }
    @SuppressWarnings("unchecked")
    public <T> T[] getModels(Class<T> modelClass) {
        LinkedList<T> models = new LinkedList<>();
        for (Modelable modelable : getModels()) {
            if (modelClass.isInstance(modelable)) {
                models.add((T) modelable);
            }
        }
        return models.toArray((T[]) Array.newInstance(modelClass, models.size()));
    }
    private ModelsListener _modelsListener;
    public void setModelsListener(ModelsListener modelsListener) {
        _modelsListener = modelsListener;
    }
    public ModelsListener getModelsListener() {
        return _modelsListener;
    }
}
