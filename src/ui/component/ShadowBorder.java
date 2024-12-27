package ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class ShadowBorder implements Border {
    private static final Color _defaultShadowColor = Color.BLACK;
    private static final int _defaultOffset = 2;
    private Color _shadowColor;
    private int _offsetX, _offsetY;
    public Color getShadowColor() {
        return _shadowColor;
    }
    public void setShadowColor(Color shadowColor) {
        _shadowColor = shadowColor;
    }
    public int getOffsetX() {
        return _offsetX;
    }
    public void setOffsetX(int offsetX) {
        _offsetX = offsetX;
    }
    public int getOffsetY() {
        return _offsetY;
    }
    public void setOffsetY(int offsetY) {
        _offsetY = offsetY;
    }
    public ShadowBorder() {
        this(_defaultOffset, _defaultOffset, _defaultShadowColor);
    }
    public ShadowBorder(Color shadowColor) {
        this(_defaultOffset, _defaultOffset, shadowColor);
    }
    public ShadowBorder(int offset) {
        this(offset, offset, _defaultShadowColor);
    }
    public ShadowBorder(int offsetX, int offsetY) {
        this(offsetX, offsetY, _defaultShadowColor);
    }
    public ShadowBorder(int offsetX, int offsetY, Color shadowColor) {
        _offsetX = offsetX;
        _offsetY = offsetY;
        _shadowColor = shadowColor;
    }
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(_shadowColor);
        g.fillRect(x + width - _offsetX, _offsetY, _offsetX, height);
        g.fillRect(_offsetX, y + height - _offsetY, width, _offsetY);
    }
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, _offsetX, _offsetY);
    }
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
