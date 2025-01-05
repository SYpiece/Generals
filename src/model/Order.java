package model;

import java.awt.Point;
import java.io.Serializable;

public final class Order implements Serializable {
	private final int _x;
    private final int _y;
    private final Direction _direction;
    private final Player _operator;
    public Order(int x, int y, Direction direction, Player operator) {
        this._x = x;
        this._y = y;
        this._direction = direction;
        _operator = operator;
    }
    public Order(Point point, Direction direction, Player operator) {
        this(point.x, point.y, direction, operator);
    }
    public Order(Point lastPoint, Point newPoint, Player operator) {
        if (lastPoint.distance(newPoint) > 1) {
            throw new IllegalArgumentException();
        }
        _x = lastPoint.x;
        _y = lastPoint.y;
        if (newPoint.x > lastPoint.x) {
            _direction = Direction.right;
        } else if (newPoint.x < lastPoint.x) {
            _direction = Direction.left;
        } else if (newPoint.y > lastPoint.y) {
            _direction = Direction.up;
        } else {
            _direction = Direction.down;
        }
        _operator = operator;
    }
    public Player getOperator() {
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
            case left: {
                return _x - 1;
            }
            case right: {
                return _x + 1;
            }
            case up:
            case down: {
                return _x;
            }
            default:
                break;
        }
        return 0;
    }
    public int toY() {
        switch (_direction) {
            case left:
            case right: {
                return _y;
            }
            case up: {
                return _y - 1;
            }
            case down: {
                return _y + 1;
            }
            default:
                break;
        }
        return 0;
    }
    public Point toPoint() {
        switch (_direction) {
            case left: {
                return new Point(_x - 1, _y);
            }
            case right: {
                return new Point(_x + 1, _y);
            }
            case up: {
                return new Point(_x, _y - 1);
            }
            case down: {
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
