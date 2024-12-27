package ui.event;

public interface GameListener {
    public void gameStarted(GameEvent e);
    public void gameCrashed(GameEvent e);
    public void gameEnded(GameEvent e);
    public void gameLost(GameEvent e);
    public void gameWon(GameEvent e);
}
