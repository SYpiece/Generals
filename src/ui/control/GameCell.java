package ui.control;

import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import model.Block;
import model.Direction;
import model.Player;
import resource.ColorResource;
import resource.ImageResource;
import util.model.IdentityDocument;

public class GameCell {
    private final Block _block;
    private double _cellX, _cellY, _cellWidth, _cellHeight;
    private boolean _leftOrderLabelVisible, _rightOrderLabelVisible, _upOrderLabelVisible, _downOrderLabelVisible, _attackable, _visible, _selected;
    private final GamePane _gamePane;
    public Block getBlock() {
        return _block;
    }
    public GamePane getGamePane() {
        return _gamePane;
    }
    public GameCell(Block block, GamePane gamePane) {
        _block = block;
        _gamePane = gamePane;
        setVisible(true);
    }
    public boolean isOrderVisible(Direction direction) {
        switch (direction) {
            case left:
                return _leftOrderLabelVisible;
            case right:
                return _rightOrderLabelVisible;
            case up:
                return _upOrderLabelVisible;
            case down:
                return _downOrderLabelVisible;
            default:
                return false;
        }
    }
    public void setOrderVisible(Direction direction, boolean visible) {
        switch (direction) {
            case left:
                _leftOrderLabelVisible = visible;
                break;
            case right:
                _rightOrderLabelVisible = visible;
                break;
            case up:
                _upOrderLabelVisible = visible;
                break;
            case down:
                _downOrderLabelVisible = visible;
                break;
        }
    }
    public boolean isUpOrderVisible() {
        return isOrderVisible(Direction.up);
    }
    public boolean isRightOrderVisible() {
        return isOrderVisible(Direction.right);
    }
    public boolean isDownOrderVisible() {
        return isOrderVisible(Direction.down);
    }
    public boolean isLeftOrderVisible() {
        return isOrderVisible(Direction.left);
    }
    public void setUpOrderVisible(boolean visible) {
        setOrderVisible(Direction.up, visible);
    }
    public void setRightOrderVisible(boolean visible) {
        setOrderVisible(Direction.right, visible);
    }
    public void setDownOrderVisible(boolean visible) {
        setOrderVisible(Direction.down, visible);
    }
    public void setLeftOrderVisible(boolean visible) {
        setOrderVisible(Direction.left, visible);
    }
    public boolean isAttackable() {
        return _attackable;
    }
    public void setAttackable(boolean attackable) {
        _attackable = attackable;
    }
    public boolean isVisible() {
        return _visible;
    }
    public void setVisible(boolean visible) {
        _visible = visible;
    }
    public boolean isSelected() {
        return _selected;
    }
    public void setSelected(boolean selected) {
        _selected = selected;
    }
    public void revalidate() {
    }
    public void setBounds(double x, double y, double width, double height) {
        _cellX = x;
        _cellY = y;
        _cellWidth = width;
        _cellHeight = height;
    }
    public void paint(GraphicsContext gc) {
        if (_selected) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
        } else {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
        }
        gc.strokeRect(_cellX, _cellY, _cellWidth, _cellHeight);
        Player.GamePlayer player = _gamePane.getClient().getGameSetting().getPlayer(_block.getOwnerID());
        if (_visible) {
            if (player == null) {
                gc.setFill(Color.web("#dcdcdc"));
            } else {
                gc.setFill(ColorResource.getColor(player.getColor()));
            }
        } else {
            gc.setFill(Color.rgb(255, 255, 255, .1));
        }
        gc.fillRect(_cellX + 1, _cellY + 1, _cellWidth - 2, _cellHeight - 2);
        gc.drawImage(ImageResource.getBlockImage(_block, _visible), _cellX + 1 + _cellWidth * .1, _cellY + 1 + _cellHeight * .1, _cellWidth - 2 - _cellWidth * .2, _cellHeight - 2 - _cellHeight * .2);
        gc.setFill(Color.WHITE);
        if (_visible && player != null) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(String.valueOf(_block.getPeople()), _cellX + _cellWidth / 2, _cellY + _cellHeight / 2, _cellWidth - 2);
        }
        if (_leftOrderLabelVisible) {
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText("←", _cellX + 1, _cellY + _cellHeight / 2, _cellWidth - 2);
        }
        if (_rightOrderLabelVisible) {
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText("→", _cellX + _cellWidth - 1, _cellY + _cellHeight / 2, _cellWidth - 2);
        }
        if (_upOrderLabelVisible) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.TOP);
            gc.fillText("↑", _cellX + _cellWidth / 2, _cellY + 1, _cellWidth - 2);
        }
        if (_downOrderLabelVisible) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.BOTTOM);
            gc.fillText("↓", _cellX + _cellWidth / 2, _cellY + _cellHeight - 1, _cellWidth - 2);
        }
        if (_attackable) {
            gc.setFill(Color.rgb(0, 0, 0, .4));
            gc.fillRect(_cellX + 1, _cellY + 1, _cellWidth - 2, _cellHeight - 2);
        }
    }
}
