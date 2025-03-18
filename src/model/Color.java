package model;

import java.io.Serializable;

public enum Color implements Serializable {
    RED(255, 0, 0),
    LIGHT_BLUE(67, 99, 216),
    GREEN(0, 128, 0),
    TEAL(0, 128, 128),
    ORANGE(245, 130, 128),
    PINK(240, 50, 230),
    PURPLE(128, 0, 128),
    MAROON(128, 0, 0),
    YELLOW(176, 159, 48),
    BROWN(154, 99, 36),
    BLUE(0, 0, 255),
    PURPLE_BLUE(72, 61, 139),
    GRAY(128, 128, 128);

    public static Color getBotColor() {
        return GRAY;
    }

    private final int _rgba;

    public int getRgba() {
        return _rgba;
    }

    public int getRgb() {
        return _rgba >> 8;
    }

    public int getRed() {
        return (_rgba >> 24) & 0xff;
    }

    public int getGreen() {
        return (_rgba >> 16) & 0xff;
    }

    public int getBlue() {
        return (_rgba >> 8) & 0xff;
    }

    public int getAlpha() {
        return _rgba & 0xff;
    }

    private Color(int red, int green, int blue) {
        this(red, blue, green, 255);
    }

    private Color(int red, int green, int blue, int alpha) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255 || alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException();
        }
        _rgba = ((red & 0xff) << 24) | ((green & 0xff) << 16) | ((blue & 0xff) << 8) | (alpha & 0xff);
    }
}
