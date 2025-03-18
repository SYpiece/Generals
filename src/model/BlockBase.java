package model;

public abstract class BlockBase implements Block {
    protected int x, y, people;
    protected DefaultPlayer owner;

    public BlockBase(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public DefaultPlayer getOwner() {
        return owner;
    }

    public void setOwner(DefaultPlayer player) {
        owner = player;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
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
