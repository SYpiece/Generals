package util.model;

public interface ModelsListener {
    public void modelCreated(ModelsManageEvent event);
    public void modelChanged(ModelsManageEvent event);
    public void modelManaged(ModelsManageEvent event);
    public Modelable modelNotManaged(ModelsManageEvent event);
    public void modelConflicted(ModelsManageEvent event);
}
