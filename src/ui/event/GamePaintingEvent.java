package ui.event;

import util.model.Modelable;

public final class GamePaintingEvent {
    private final Modelable _model;
    public Modelable getModel() {
        return _model;
    }
    public GamePaintingEvent(Modelable model) {
        _model = model;
    }
}
