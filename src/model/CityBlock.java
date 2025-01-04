package model;

public final class CityBlock extends ObstacleBlock {
    private static final int updateCount = 2;
    private boolean _isCrown;
	public CityBlock(int x, int y) {
        this(x, y, false);
    }
    public CityBlock(int x, int y, boolean isCrown) {
        super(x, y);
        _isCrown = isCrown;
    }
    public boolean isCrown() {
        return _isCrown;
    }
    public void beenCaptured() {
        _isCrown = false;
    }
//    @Override
//    public Image getImage(boolean isFound) {
//        if (isFound) {
//            if (_isCrown) {
//                return ImageResource.crownImage;
//            } else {
//                return ImageResource.cityImage;
//            }
//        } else {
//            if (_isCrown) {
//                return ImageResource.emptyImage;
//            } else {
//                return ImageResource.obstacleImage;
//            }
//        }
//    }
//    @Override
//    public Color getColor(boolean isFound) {
//        if (isFound) {
//            return _owner.getColor();
//        } else {
//            return Color.LIGHT_GRAY;
//        }
//    }
    @Override
    public boolean update(Game game) {
        if (_owner != game.getBot() && (game.getGameTick() % updateCount) == 0) {
            _people++;
            return true;
        } else {
            return false;
        }
    }
//    @Override
//    public ModelProperty[] getModelProperties(ModelsManager manager) {
//        ModelProperty[] modelProperties = super.getModelProperties(manager);
//        ModelProperty[] newModelProperties = new ModelProperty[modelProperties.length + 1];
//        newModelProperties[newModelProperties.length - 1] = manager.createProperty(this, "isCrown", _isCrown);
//        return newModelProperties;
//    }
//    @Override
//    public void setModelProperties(ModelsManager manager, ModelProperty[] properties) {
//        for (ModelProperty property : properties) {
//            setModelProperty(manager, property);
//        }
//    }
//    @Override
//    public ModelProperty getModelProperty(ModelsManager manager, String key) {
//        if (key.equals("isCrown")) {
//            return manager.createProperty(this, key, _isCrown);
//        } else {
//            return super.getModelProperty(manager, key);
//        }
//    }
//    @Override
//    public void setModelProperty(ModelsManager manager, ModelProperty property) {
//        switch (property.key()) {
//            case "isCrwon":
//                _isCrown = (boolean) property.originalValue();
//                break;
//            default:
//                super.setModelProperty(manager, property);
//                break;
//        }
//    }
//    public CityBlockSummary getSummary() {
//        return new CityBlockSummary(this);
//    }
//    public static class CityBlockSummary extends BlockSummary {
//        protected final boolean _isCrown;
//        public boolean isCrown() {
//            return _isCrown;
//        }
//        public CityBlockSummary(City city) {
//            super(city);
//            _isCrown = city.isCrown();
//        }
//    }
}
