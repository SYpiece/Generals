package util.model;

public interface ExternModelable extends Modelable {
    public ModelProperty[] getModelProperties(ModelsManager manager);
    public void setModelProperties(ModelsManager manager, ModelProperty[] properties);
    public ModelProperty getModelProperty(ModelsManager manager, String key);
    public void setModelProperty(ModelsManager manager, ModelProperty property);
}
