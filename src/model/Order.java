package model;

import java.awt.Point;
import java.io.Serializable;

public final class Order implements Serializable {
	private final int _x;
    private final int _y;
    private final Direction _direction;
    private final DefaultPlayer _operator;
    public Order(int x, int y, Direction direction, DefaultPlayer operator) {
        this._x = x;
        this._y = y;
        this._direction = direction;
        _operator = operator;
    }
    public Order(Point point, Direction direction, DefaultPlayer operator) {
        this(point.x, point.y, direction, operator);
    }
    public Order(Point lastPoint, Point newPoint, DefaultPlayer operator) {
        if (lastPoint.distance(newPoint) > 1) {
            throw new IllegalArgumentException();
        }
        _x = lastPoint.x;
        _y = lastPoint.y;
        if (newPoint.x > lastPoint.x) {
            _direction = Direction.Right;
        } else if (newPoint.x < lastPoint.x) {
            _direction = Direction.Left;
        } else if (newPoint.y > lastPoint.y) {
            _direction = Direction.Up;
        } else {
            _direction = Direction.Down;
        }
        _operator = operator;
    }
    public DefaultPlayer getOperator() {
        return _operator;
    }
    public int fromX() {
        return _x;
    }
    public int fromY() {
        return _y;
    }
    public Point fromPoint() {
        return new Point(_x, _y);
    }
    public int toX() {
        switch (_direction) {
            case Left: {
                return _x - 1;
            }
            case Right: {
                return _x + 1;
            }
            case Up:
            case Down: {
                return _x;
            }
            default:
                break;
        }
        return 0;
    }
    public int toY() {
        switch (_direction) {
            case Left:
            case Right: {
                return _y;
            }
            case Up: {
                return _y - 1;
            }
            case Down: {
                return _y + 1;
            }
            default:
                break;
        }
        return 0;
    }
    public Point toPoint() {
        switch (_direction) {
            case Left: {
                return new Point(_x - 1, _y);
            }
            case Right: {
                return new Point(_x + 1, _y);
            }
            case Up: {
                return new Point(_x, _y - 1);
            }
            case Down: {
                return new Point(_x, _y + 1);
            }
            default:
                break;
        }
        return new Point();
    }
    public Direction getDirection() {
        return _direction;
    }
}
