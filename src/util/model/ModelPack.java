package util.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class ModelPack implements Serializable {
//    protected boolean containsModel(Model model) {
//    }
//    protected ModelSummary addModel(ModelManager manager, Model model) {
//        _summaries.
//        ModelSummary summary = new ModelSummary(manager, this, model);
//        _summaries.add(new ModelSummary(manager, this, model));
//    }
    protected ModelManager _manager;
    protected Model _model;
    protected Type _type;
    protected List<ModelSummary> _summaries;
    protected ModelPack(ModelManager manager, Model model) {
        _manager = manager;
        _model = model;
        _type = Type.Sent;
    }
    protected Model analyzeModel(ModelManager manager) {
        if (_type != Type.Received) {
            throw new RuntimeException();
        } else {
            Set<Model> models = new HashSet<>();
            Model model;
            for (Iterator<ModelSummary> iterator = _summaries.iterator(); iterator.hasNext();) {
                ModelSummary summary = iterator.next();
                models.add()
                if (!iterator.hasNext()) {
                    model =
                }
            }
            return model;
        }
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (_type != Type.Sent) {
            throw new IOException();
        } else {
            List<ModelSummary> summaries = new LinkedList<>();
            summaries.add(new ModelSummary());
            out.writeObject(summaries);
        }
    }
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        _type = Type.Received;
        _summaries = (List<ModelSummary>) in.readObject();
    }
    public enum Type {
        Sent, Received,
    }
}
