package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 5455073415064420104L;
    protected String _name;
    protected int _level;
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }
    public int getLevel() {
        return _level;
    }
    public void setLevel(int level) {
        _level = level;
    }
    public User() {
        this("player", 0);
    }
    public User(String name) {
        this(name, 0);
    }
    public User(String name, int level) {
        setName(name);
        setLevel(level);
    }
}
