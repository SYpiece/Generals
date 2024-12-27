package model;

import java.awt.Point;
import java.io.Serializable;

import util.model.IdentityDocument;

public final class Order implements Serializable {
	private final int _x;
    private final int _y;
    private final Direction _direction;
    private final IdentityDocument _operatorID;
    public Order(int x, int y, Direction direction, IdentityDocument operatorID) {
        this._x = x;
        this._y = y;
        this._direction = direction;
        _operatorID = operatorID;
//        _id = IdentityDocument.createID();
    }
    public Order(Point point, Direction direction, IdentityDocument operatorID) {
        this(point.x, point.y, direction, operatorID);
    }
    public Order(Point lastPoint, Point newPoint, IdentityDocument operatorID) {
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
        _operatorID = operatorID;
//        _id = IdentityDocument.createID();
    }
//    protected Order(IdentityDocument id) {
//        _id = id;
//    }
//    public Player operator() {
//        return _operator;
//    }
    public IdentityDocument getOperatorID() {
        return _operatorID;
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
//    private final IdentityDocument _id;
//    public IdentityDocument getID() {
//        return _id;
//    }
}
