package ui.control;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import model.Direction;
import model.DefaultGame;
import model.DefaultMap;
import model.Order;
import socket.DefaultGameClient;

public class GamePane extends Canvas {
    public final static int blockMinWidthAndHeight = 15, blockMaxWidthAndHeight = 250, dragMoveDistance = 10;
    private final DefaultGameClient _gameClient;
    private double _tableX, _tableY, _tableWidth, _tableHeight;
    private final GameCell[][] _cells;
    public DefaultGameClient getClient() {
        return _gameClient;
    }
    public DefaultGame getGame() {
        return _gameClient.getGame();
    }
    public DefaultMap getMap() {
        return _gameClient.getGame().getMap();
    }
    public int mapWidth() {
        return _gameClient.getGame().getMapWidth();
    }
    public int mapHeight() {
        return _gameClient.getGame().getMapHeight();
    }
    public double getTableX() {
        return _tableX;
    }
    public double getTableY() {
        return _tableY;
    }
    public double getTableWidth() {
        return _tableWidth;
    }
    public double getTableHeight() {
        return _tableHeight;
    }
    public void setTableX(double x) {
        _tableX = x;
        revalidate();
    }
    public void setTableY(double y) {
        _tableY = y;
        revalidate();
    }
    public void setTableWidth(double width) {
        _tableWidth = width;
        revalidate();
    }
    public void setTableHeight(double height) {
        _tableHeight = height;
        revalidate();
    }
    private double _mousePressX, _mousePressY, _mousePressOffsetX, _mousePressOffsetY;
    private boolean _isDragging;
    private int _selectedX = -1, _selectedY = -1;
    public GamePane(DefaultGameClient gameClient) {
        _gameClient = gameClient;
        _cells = new GameCell[mapWidth()][mapHeight()];
        DefaultMap map = gameClient.getGame().getMap();
        for (int x = 0; x < mapWidth(); x++) {
            for (int y = 0; y < mapHeight(); y++) {
                GameCell cell = new GameCell(map.getBlockAt(x, y), this);
                _cells[x][y] = cell;
            }
        }
        revalidate();
        widthProperty().addListener(observable -> {
            repaint();
        });
        heightProperty().addListener(observable -> {
            repaint();
        });
        setOnMousePressed(event -> {
            _mousePressX = event.getX();
            _mousePressY = event.getY();
            _mousePressOffsetX = _mousePressX - _tableX;
            _mousePressOffsetY = _mousePressY - _tableY;
            _isDragging = false;
            event.consume();
        });
        setOnMouseDragged(event -> {
            double mouseX = event.getX(), mouseY = event.getY();
            if (distance(mouseX, mouseY, _mousePressX, _mousePressY) > dragMoveDistance) {
                _isDragging = true;
            }
            if (_isDragging) {
                _tableX = mouseX - _mousePressOffsetX;
                _tableY = mouseY - _mousePressOffsetY;
            }
            revalidate();
            repaint();
            event.consume();
        });
        setOnMouseReleased(event -> {
            if (!_isDragging) {
                int selectedX = (int) (_mousePressOffsetX / _tableWidth * mapWidth()), selectedY = (int) (_mousePressOffsetY / _tableHeight * mapHeight());
                cancelSelectedPoint();
                if (0 <= selectedX && selectedX < mapWidth() && 0 <= selectedY && selectedY < mapHeight()) {
                    selectPoint(selectedX, selectedY);
                }
                repaint();
            }
            event.consume();
        });
        setOnScroll(event -> {
            double rotation = 1 + event.getDeltaY() / 100, centerX = getWidth() / 2, centerY = getHeight() / 2;
            double newWidth = _tableWidth * rotation, newHeight = _tableHeight * rotation;
            if (newWidth < mapWidth() * blockMinWidthAndHeight || newHeight < mapHeight() * blockMinWidthAndHeight) {
                newWidth = mapWidth() * blockMinWidthAndHeight;
                newHeight = mapHeight() * blockMinWidthAndHeight;
            } else if (newWidth > mapWidth() * blockMaxWidthAndHeight || newHeight > mapHeight() * blockMaxWidthAndHeight) {
                newWidth = mapWidth() * blockMaxWidthAndHeight;
                newHeight = mapHeight() * blockMaxWidthAndHeight;
            }
            _tableX = centerX - (centerX - _tableX) / _tableWidth * newWidth;
            _tableY = centerY - (centerY - _tableY) / _tableHeight * newHeight;
            _tableWidth = newWidth;
            _tableHeight = newHeight;
            revalidate();
            repaint();
            event.consume();
        });
        setFocusTraversable(true);
        setFocused(true);
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    moveSelectedPoint(Direction.Up);
                    break;
                case A:
                    moveSelectedPoint(Direction.Left);
                    break;
                case S:
                    moveSelectedPoint(Direction.Down);
                    break;
                case D:
                    moveSelectedPoint(Direction.Right);
                    break;
            }
            event.consume();
        });
    }
    private void cancelSelectedPoint() {
        if (_selectedX != -1 && _selectedY != -1) {
            if (_selectedX > 0) {
                _cells[_selectedX - 1][_selectedY].setAttackable(false);
            }
            if (_selectedX < mapWidth() - 1) {
                _cells[_selectedX + 1][_selectedY].setAttackable(false);
            }
            if (_selectedY > 0) {
                _cells[_selectedX][_selectedY - 1].setAttackable(false);
            }
            if (_selectedY < mapHeight() - 1) {
                _cells[_selectedX][_selectedY + 1].setAttackable(false);
            }
            _cells[_selectedX][_selectedY].setSelected(false);
            _selectedX = -1;
            _selectedY = -1;
            repaint();
        }
    }
    private void selectPoint(int selectedX, int selectedY) {
        if (0 > selectedX || selectedX >= mapWidth() || 0 > selectedY || selectedY >= mapHeight()) {
            return;
        }
        _selectedX = selectedX;
        _selectedY = selectedY;
        if (_selectedX > 0) {
            _cells[_selectedX - 1][_selectedY].setAttackable(true);
        }
        if (_selectedX < mapWidth() - 1) {
            _cells[_selectedX + 1][_selectedY].setAttackable(true);
        }
        if (_selectedY > 0) {
            _cells[_selectedX][_selectedY - 1].setAttackable(true);
        }
        if (_selectedY < mapHeight() - 1) {
            _cells[_selectedX][_selectedY + 1].setAttackable(true);
        }
        _cells[_selectedX][_selectedY].setSelected(true);
        repaint();
    }
    private void moveSelectedPoint(Direction direction) {
        if (_selectedX == -1 && _selectedY == -1) {
            return;
        }
//        System.out.println("from x: " + _selectedX + " y: " + _selectedY);
        int selectedX = _selectedX, selectedY = _selectedY;
        cancelSelectedPoint();
        switch (direction) {
            case Left:
                if (selectedX > 0) {
                    selectPoint(selectedX - 1, selectedY);
                    return;
                }
                break;
            case Right:
                if (selectedX < mapWidth() - 1) {
                    selectPoint(selectedX + 1, selectedY);
                    return;
                }
                break;
            case Up:
                if (selectedY > 0) {
                    selectPoint(selectedX, selectedY - 1);
                    return;
                }
                break;
            case Down:
                if (selectedY < mapHeight() - 1) {
                    selectPoint(selectedX, selectedY + 1);
                    return;
                }
                break;
            default:
                break;
        }
        selectPoint(selectedX, selectedY);
    }
    public void revalidate() {
        double cellWidth = _tableWidth / mapWidth(), cellHeight = _tableHeight / mapHeight();
        double cellX = _tableX;
        for (int x = 0; x < mapWidth(); x++, cellX += cellWidth) {
            double cellY = _tableY;
            for (int y = 0; y < mapHeight(); y++, cellY += cellHeight) {
                GameCell cell = _cells[x][y];
                cell.setBounds(cellX, cellY, cellWidth - 1, cellHeight - 1);
                cell.revalidate();
            }
        }
    }
    public void repaint() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        for (int x = 0; x < mapWidth(); x++) {
            for (int y = 0; y < mapHeight(); y++) {
                _cells[x][y].setUpOrderVisible(false);
                _cells[x][y].setRightOrderVisible(false);
                _cells[x][y].setDownOrderVisible(false);
                _cells[x][y].setLeftOrderVisible(false);
            }
        }
        for (Order order : _gameClient.getAllOrders()) {
            _cells[order.fromX()][order.fromY()].setOrderVisible(order.getDirection(), true);
        }
        double fontSize = 10 + (_tableWidth - mapWidth() * blockMinWidthAndHeight) / mapWidth() / 6;
        if (fontSize > 20) {
            fontSize = 20;
        }
        gc.setFont(Font.font("quicksand", fontSize));
        for (int x = 0; x < mapWidth(); x++) {
            for (int y = 0; y < mapHeight(); y++) {
                _cells[x][y].paint(gc);
            }
        }
    }
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    @Override
    public boolean isResizable() {
        return true;
    }
}
