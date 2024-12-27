package model;

import java.io.Serializable;

import resource.color.Color;
import util.model.IdentityDocument;

public class Player {
    protected PlayerInformation _information;
    public PlayerInformation getInformation() {
        return _information;
    }
    public String getName() {
        return _information.getName();
    }
    public void setName(String name) {
        _information.setName(name);
    }
    public int getLevel() {
        return _information.getLevel();
    }
    public void setLevel(int level) {
        _information.setLevel(level);
    }
    public Player() {
        this(new PlayerInformation());
    }
    public Player(String name, int level) {
        this(new PlayerInformation(name, level));
    }
    public Player(PlayerInformation information) {
        _information = information;
    }
    public static class GamePlayer extends Player implements Serializable {
        public static GamePlayer createBot() {
            return new GamePlayer(new PlayerInformation("", 0), Color.getBotColor(), -1, true, false);
        }
        public static GamePlayer createNone() {
            return new GamePlayer(new PlayerInformation("", 0), null, -1, false, false);
        }
        public static GamePlayer createCompetitor(PlayerInformation information, Color color, int team) {
            if (team <= 0) {
                throw new IllegalArgumentException("team should be greater than 0");
            }
            return new GamePlayer(information, color, team, true, true);
        }
        public static GamePlayer createSpectator(PlayerInformation information, Color color) {
            return new GamePlayer(information, color, 0, false, true);
        }
        protected int _team;
        protected Color _color;
        protected boolean _alive, _real;
        protected boolean _forceStart = false;
        protected final IdentityDocument _id = IdentityDocument.createID();
        public int getTeam() {
            return _team;
        }
        public void setTeam(int team) {
            _team = team;
        }
        public Color getColor() {
            return _color;
        }
        public void setColor(Color color) {
            _color = color;
        }
        public boolean isAlive() {
            return _alive;
        }
        public void setAlive(boolean alive) {
            _alive = alive;
        }
        public boolean isReal() {
            return _real;
        }
        public boolean getForceState() {
            return _forceStart;
        }
        public void setForceStart(boolean forceStart) {
            _forceStart = forceStart;
        }
        public IdentityDocument getID() {
            return _id;
        }
        protected GamePlayer(PlayerInformation information, Color color, int team, boolean isAlive, boolean isReal) {
            super(information);
            setColor(color);
            setTeam(team);
            setAlive(isAlive);
            _real = isReal;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GamePlayer) {
                return _id.equals(((GamePlayer) obj)._id);
            } else {
                return super.equals(obj);
            }
        }
    }
    public static final class PlayerInformation implements Serializable {
        private String _name;
        private int _level;
        public void setName(String name) {
            _name = name;
        }
        public String getName() {
            return _name;
        }
        public void setLevel(int level) {
            _level = level;
        }
        public int getLevel() {
            return _level;
        }
        public PlayerInformation() {
            this("Anonynous", 0);
        }
        public PlayerInformation(String name, int level) {
            _name = name;
            _level = level;
//            _id = IdentityDocument.createID();
        }
//        protected PlayerInformation(IdentityDocument id) {
//            _id = id;
//        }
//        protected IdentityDocument _id;
//        @Override
//        public IdentityDocument getID() {
//            return _id;
//        }
    }
}
