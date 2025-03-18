package model;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class Configuration implements Serializable {
    protected final Map<String, Object> _configs = new HashMap<>();

    public Configuration with(String key, Object value) {
        put(key, value);
        return this;
    }

    public void put(String key, Object value) {
        _configs.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) _configs.get(key);
    }

    public boolean contains(String key) {
        return _configs.containsKey(key);
    }
}
