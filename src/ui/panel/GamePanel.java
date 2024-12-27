package ui.panel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import model.Game;
import model.Order;
import socket.GameClient;
import socket.GameStatus;
import ui.component.GameMapTable;

public class GamePanel extends JPanel {
    private final GameClient _gameClient;
    private final GameMapTable _gameTable;
    public GamePanel(GameClient gameClient) {
        if (gameClient.getGameState() != GameStatus.Running) {
            throw new IllegalArgumentException("game is not started");
        }
        _gameClient = gameClient;
        _gameClient.addGameListener(new GameAdapter() {
            @Override
            public void gameModelCreating(GameEvent e) {
                _gameTable.repaint();
            }
            @Override
            public void gameModelChanged(GameEvent e) {
                _gameTable.repaint();
            }
            @Override
            public void gameModelReleased(GameEvent e) {
                _gameTable.repaint();
            }
        });
        _gameTable = new GameMapTable(_gameClient.getGame().getMap());
        initComponents();
    }
    public GamePanel(Game game) {
        _gameClient = null;
        _gameTable = new GameMapTable(game.getMap());
        initComponents();
    }
    private Point _lastClickPoint;
    private void initComponents() {
        setLayout(null);
        {
            _gameTable.setBounds(0, 0, _gameTable.getRowCount() * 30, _gameTable.getColumnCount() * 30);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                private Point _mousePoint;
                private boolean _isDraging;
                @Override
                public void mousePressed(MouseEvent e) {
                    _mousePoint = e.getPoint();
                    _isDraging = false;
                }
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (_isDraging) {
                        _gameTable.setLocation(-_mousePoint.x + _gameTable.getX() + e.getX(), -_mousePoint.y + _gameTable.getY() + e.getY());
                        _lastClickPoint = null;
                    } else {
                        if (e.getPoint().distance(_mousePoint) > GameMapTable.dragMoveDistance) {
                            _isDraging = true;
                        }
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!_isDraging) {
                        Point newClickPoint = new Point(_gameTable.columnAtPoint(_mousePoint), _gameTable.rowAtPoint(_mousePoint));
                        if (_lastClickPoint != null && _lastClickPoint.distanceSq(newClickPoint) == 1) {
                            _gameClient.addOrder(new Order(_lastClickPoint, newClickPoint, _gameClient.operator()));
                        }
                        _lastClickPoint = newClickPoint;
                    }
                }
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double rotation = 1 - e.getPreciseWheelRotation() / 20;
                    Dimension newSize = new Dimension((int)(_gameTable.getWidth() * rotation), (int)(_gameTable.getHeight() * rotation));
                    if (newSize.getWidth() < _gameTable.getColumnCount() * GameMapTable.blockMinWidthAndHeight || newSize.getHeight() < _gameTable.getRowCount() * GameMapTable.blockMinWidthAndHeight) {
                        newSize.width = _gameTable.getColumnCount() * GameMapTable.blockMinWidthAndHeight;
                        newSize.height = _gameTable.getRowCount() * GameMapTable.blockMinWidthAndHeight;
                    } else if (newSize.getWidth() > _gameTable.getColumnCount() * GameMapTable.blockMaxWidthAndHeight || newSize.getHeight() > _gameTable.getRowCount() * GameMapTable.blockMaxWidthAndHeight) {
                        newSize.width = _gameTable.getColumnCount() * GameMapTable.blockMaxWidthAndHeight;
                        newSize.height = _gameTable.getRowCount() * GameMapTable.blockMaxWidthAndHeight;
                    }
                    _gameTable.setSize(newSize);
                    int columnY = newSize.height;
                    for (int lastHeight = _gameTable.getHeight(), lastRow = _gameTable.getRowCount(); lastRow > 0; lastHeight -= lastHeight / lastRow--) {
                        if (lastRow > _lastBlockY) {
                            columnY -= lastHeight / lastRow;
                        }
                        _gameTable.setRowHeight(lastRow - 1, lastHeight / lastRow);
                    }
                    int columnX = newSize.width;
                    for (int lastWidth = _gameTable.getWidth(), lastColumn = _gameTable.getColumnCount(); lastColumn > 0; lastWidth -= lastWidth / lastColumn--) {
                        if (lastColumn > _lastBlockX) {
                            columnX -= lastWidth / lastColumn;
                        }
                        _gameTable.getColumnModel().getColumn(lastColumn - 1).setPreferredWidth(lastWidth / lastColumn);
                    }
                    Rectangle lastBlockRect = new Rectangle(columnX, columnY, _gameTable.getColumnModel().getColumn(_lastBlockX).getPreferredWidth(), _gameTable.getRowHeight(_lastBlockY));
                    _gameTable.setLocation(_gameTable.getX() - lastBlockRect.x + e.getX() - (int)(_lastOffsetX * lastBlockRect.width), _gameTable.getY() - lastBlockRect.y + e.getY() - (int)(_lastOffsetY * lastBlockRect.height));
                    _gameTable.revalidate();
                }
                private int _lastBlockX, _lastBlockY;
                private double _lastOffsetX, _lastOffsetY;
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point mousePoint = e.getPoint();
                    _lastBlockX = _gameTable.columnAtPoint(mousePoint);
                    _lastBlockY = _gameTable.rowAtPoint(mousePoint);
                    Rectangle blockRect = _gameTable.getCellRect(_lastBlockY, _lastBlockX, true);
                    _lastOffsetX = (double)(e.getX() - blockRect.x) / blockRect.width;
                    _lastOffsetY = (double)(e.getY() - blockRect.y) / blockRect.height;
                }
            };
            _gameTable.addMouseListener(mouseAdapter);
            _gameTable.addMouseMotionListener(mouseAdapter);
            _gameTable.addMouseWheelListener(mouseAdapter);
            add(_gameTable);
        }
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int _oldX, _oldY;
            @Override
            public void mousePressed(MouseEvent e) {
                _oldX = _gameTable.getX() - e.getX();
                _oldY = _gameTable.getY() - e.getY();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                _gameTable.setLocation(_oldX + e.getX(), _oldY + e.getY());
            }
            private double _lastOffsetX, _lastOffsetY;
            @Override
            public void mouseMoved(MouseEvent e) {
                _lastOffsetX = (double)(e.getX() - _gameTable.getX()) / _gameTable.getWidth();
                _lastOffsetY = (double)(e.getY() - _gameTable.getY()) / _gameTable.getHeight();
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double rotation = 1 - e.getPreciseWheelRotation() / 20;
                Dimension newSize = new Dimension((int)(_gameTable.getWidth() * rotation), (int)(_gameTable.getHeight() * rotation));
                if (newSize.getWidth() < _gameTable.getColumnCount() * GameMapTable.blockMinWidthAndHeight || newSize.getHeight() < _gameTable.getRowCount() * GameMapTable.blockMinWidthAndHeight) {
                    newSize.width = _gameTable.getColumnCount() * GameMapTable.blockMinWidthAndHeight;
                    newSize.height = _gameTable.getRowCount() * GameMapTable.blockMinWidthAndHeight;
                } else if (newSize.getWidth() > _gameTable.getColumnCount() * GameMapTable.blockMaxWidthAndHeight || newSize.getHeight() > _gameTable.getRowCount() * GameMapTable.blockMaxWidthAndHeight) {
                    newSize.width = _gameTable.getColumnCount() * GameMapTable.blockMaxWidthAndHeight;
                    newSize.height = _gameTable.getRowCount() * GameMapTable.blockMaxWidthAndHeight;
                }
                _gameTable.setSize(newSize);
                for (int lastHeight = _gameTable.getHeight(), lastRow = _gameTable.getRowCount(); lastRow > 0; lastHeight -= lastHeight / lastRow--) {
                    _gameTable.setRowHeight(lastRow - 1, lastHeight / lastRow);
                }
                for (int lastWidth = _gameTable.getWidth(), lastColumn = _gameTable.getColumnCount(); lastColumn > 0; lastWidth -= lastWidth / lastColumn--) {
                    _gameTable.getColumnModel().getColumn(lastColumn - 1).setPreferredWidth(lastWidth / lastColumn);
                }
                _gameTable.setLocation(e.getX() - (int)(_lastOffsetX * _gameTable.getWidth()), e.getY() - (int)(_lastOffsetY * _gameTable.getHeight()));
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }
}
