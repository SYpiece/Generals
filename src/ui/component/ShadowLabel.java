package ui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class ShadowLabel extends JLabel implements Shadowable {
    private static final Color _defaultShadowColor = Color.BLACK;
    private static final int _translateDistance = 2;
    protected Color _shadowColor;
    protected int _translateX, _translateY;
    @Override
    public Color getShadowColor() {
        return _shadowColor;
    }
    @Override
    public void setShadowColor(Color shadow) {
        _shadowColor = shadow;
    }
    @Override
    public int getOffsetX() {
        return _translateX;
    }
    @Override
    public void setOffsetX(int translateX) {
        _translateX = translateX;
    }
    @Override
    public int getOffsetY() {
        return _translateY;
    }
    @Override
    public void setOffsetY(int translateY) {
        _translateY = translateY;
    }
    public ShadowLabel() {
        this(_defaultShadowColor);
    }
    public ShadowLabel(Color shadow) {
        this(shadow, _translateDistance);
    }
    public ShadowLabel(int translate) {
        this(_defaultShadowColor, translate);
    }
    public ShadowLabel(int translateX, int translateY) {
        this(_defaultShadowColor, translateX, translateY);
    }
    public ShadowLabel(Color shadow, int translate) {
        this(shadow, translate, translate);
    }
    public ShadowLabel(Color shadow, int translateX, int translateY) {
        setShadowColor(shadow);
        setOffsetX(translateX);
        setOffsetY(translateY);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color foreground = getForeground();
        g2.translate(_translateX, _translateY);
        setForeground(_shadowColor);
        super.paintComponent(g);
        g2.translate(-_translateX, -_translateY);
        setForeground(foreground);
        super.paintComponent(g);
    }
}
