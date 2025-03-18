package model;

public interface Block extends Configurable {
    String
            xKey = "X",
            yKey = "Y",
            typeKey = "TYPE",
            ownerKey = "OWNER",
            peopleKey = "PEOPLE";

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    BlockType getBlockType();

    ImageType getImageType(boolean shown);

    Color getBackground(boolean shown);

    void setOwner(DefaultPlayer player);

    DefaultPlayer getOwner();

    void setPeople(int people);

    int getPeople();

    boolean simulate(Game game);

    @Override
    default Configuration getConfiguration() {
        return new Configuration()
                .with(xKey, getX())
                .with(yKey, getY())
                .with(typeKey, getBlockType())
                .with(ownerKey, getOwner())
                .with(peopleKey, getPeople());
    }

    @Override
    default void apply(Configuration configuration) {
        if (configuration.contains(typeKey) && configuration.get(typeKey) != getBlockType()) {
            throw new IllegalArgumentException();
        }
        if (configuration.contains(xKey)) {
            setX(configuration.get(xKey));
        }
        if (configuration.contains(yKey)) {
            setY(configuration.get(yKey));
        }
        if (configuration.contains(ownerKey)) {
            setOwner(configuration.get(ownerKey));
        }
        if (configuration.contains(peopleKey)) {
            setPeople(configuration.get(peopleKey));
        }
    }
}
