package model;

public interface Player extends Configurable {
    String
            nameKey = "NAME",
            levelKey = "LEVEL",
            colorKey = "COLOR",

    String getName();

    void setName(String name);

    int getLevel();

    void setLevel(int level);

    Color getColor();

    void setColor(Color color);

    @Override
    default Configuration getConfiguration() {
        return new Configuration()
                .with(nameKey, getName())
                .with(levelKey, getLevel())
                .with(colorKey, getColor());
    }

    @Override
    default void apply(Configuration configuration) {
        if (configuration.contains(nameKey)) {
            setName(configuration.get(nameKey));
        }
        if (configuration.contains(levelKey)) {
            setLevel(configuration.get(levelKey));
        }
        if (configuration.contains(colorKey)) {
            setColor(configuration.get(colorKey));
        }
    }
}
