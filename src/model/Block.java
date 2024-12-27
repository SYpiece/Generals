package model;

import java.awt.Dimension;
import java.io.Serializable;

import util.model.IdentityDocument;

public abstract class Block implements Serializable {
    protected final int _x;
    protected final int _y;
//    protected Block(IdentityDocument id) {
//        _id = id;
//    }
    protected IdentityDocument _ownerID;
    public Block(int x, int y) {
        _x = x;
        _y = y;
//        _id = IdentityDocument.createID();
    }
//    protected Block(BlockSummary summary) {
//
//    }
    public int getX() {
        return _x;
    }
    public int getY() {
        return _y;
    }
    public static Dimension getImageSize() {
        return new Dimension(100, 100);
    } 
    public static int getImageHeight() {
        return 100;
    }
    public static int getImageWidth() {
        return 100;
    }
    protected int _people;
//    protected Player.GamePlayer _owner;
    public int getPeople() {
        return _people;
    }
    public void setPeople(int people) {
        _people = people;
    }
    public IdentityDocument getOwnerID() {
        return _ownerID;
    }
    public void setOwnerID(IdentityDocument id) {
        _ownerID = id;
    }
    public BlockType getType() {
        if (this instanceof CityBlock) {
            return BlockType.City;
        } else if (this instanceof MountainBlock) {
            return BlockType.Mountain;
        } else if (this instanceof LandBlock) {
            return BlockType.Land;
        } else {
            return BlockType.Unknown;
        }
    }
//    public abstract Image getImage(boolean isFound);
//    public abstract Color getColor(boolean isFound);
    public abstract boolean update(Game game);
//    private final IdentityDocument _id;
//    @Override
//    public IdentityDocument getID() {
//        return _id;
//    }
//    @Override
//    public ModelProperty[] getModelProperties(ModelsManager manager) {
//        return new ModelProperty[] {manager.createProperty(this, "x", _x), manager.createProperty(this, "y", _y), manager.createProperty(this, "people", _people), manager.createProperty(this, "owner", _owner)};
//    }
//    @Override
//    public void setModelProperties(ModelsManager manager, ModelProperty[] properties) {
//        for (ModelProperty property : properties) {
//            setModelProperty(manager, property);
//        }
//    }
//    @Override
//    public ModelProperty getModelProperty(ModelsManager manager, String key) {
//        switch (key) {
//            case "x":
//                return manager.createProperty(this, key, _x);
//            case "y":
//                return manager.createProperty(this, key, _y);
//            case "people":
//                return manager.createProperty(this, key, _people);
//            case "owner":
//                return manager.createProperty(this, key, _owner);
//            default:
//                return null;
//        }
//    }
//    @Override
//    public void setModelProperty(ModelsManager manager, ModelProperty property) {
//        switch (property.key()) {
//            case "x":
//                _x = (int) property.originalValue();
//                break;
//            case "y":
//                _y = (int) property.originalValue();
//                break;
//            case "people":
//                _people = (int) property.originalValue();
//                break;
//            case "owner":
//                _owner = (Player.GamePlayer) property.modelValue(manager);
//                break;
//            default:
//                break;
//        }
//    }
//    public abstract BlockSummary getSummary();
//    public static abstract class BlockSummary {
//        protected final int _x, _y, _people;
//        protected final Player.GamePlayer _owner;
//        protected final BlockType _blockType;
//        public int getBlockX() {
//            return _x;
//        }
//        public int getBlockY() {
//            return _y;
//        }
//        public int getBlockPeople() {
//            return _people;
//        }
//        public BlockType getBlockType() {
//            return _blockType;
//        }
//        protected BlockSummary(Block block) {
//            _x = block.getX();
//            _y = block.getY();
//            _people = block.getPeople();
//            _blockType = block.getType();
//        }
//    }
}
