package model;

import java.util.List;
import java.util.Set;

public interface Game extends Configurable {
    String
            mapKey = "MAP",
            tickKey = "TICK";

    Map getMap();

    void setMap(Map map);

    int getTick();

    void setTick(int tick);

    Set<Block> simulate(List<Order> orders);

    void apply(Set<Block> blocks);

    @Override
    default Configuration getConfiguration() {
        return new Configuration()
                .with(mapKey, getMap())
                .with(tickKey, getTick());
    }

    @Override
    default void apply(Configuration configuration) {
        if (configuration.contains(mapKey)) {
            setMap(configuration.get(mapKey));
        }
        if (configuration.contains(tickKey)) {
            setTick(configuration.get(tickKey));
        }
    }
}
