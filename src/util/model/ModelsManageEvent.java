package util.model;

public class ModelsManageEvent {
    private final Modelable _model;
    private final IdentityDocument _id;
    public boolean modelNotFound() {
        return _model == null;
    }
    public Modelable model() {
        return _model;
    }
    public IdentityDocument id() {
        return _id;
    }
    public ModelsManageEvent(Modelable model) {
        _model = model;
        _id = model.getID();
    }
    public ModelsManageEvent(IdentityDocument id) {
        _model = null;
        _id = id;
    }
}
