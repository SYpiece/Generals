package model;

public class DefaultPlayer implements Player {
    protected String name;

    protected int level, id;

    protected Color color;

    protected Team _team;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public Team getTeam() {
        return _team;
    }

    protected void setTeam(Team team) {
        _team = team;
    }

    public DefaultPlayer(User user) {
        setName(user.getName());
        setLevel(user.getLevel());
    }
}
