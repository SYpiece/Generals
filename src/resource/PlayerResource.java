package resource;

import model.Player;

public final class PlayerResource {
    private static final Player _player = new Player();
    public static Player getPlayer() {
        return _player;
    }
    private PlayerResource() {}
}
