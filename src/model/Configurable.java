package model;

public interface Configurable {
    Configuration getConfiguration();
    void apply(Configuration configuration);
}
