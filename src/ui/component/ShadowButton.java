package ui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;

public class ShadowButton extends JButton implements Shadowable {
    private static final Color _defaultShadowColor = Color.BLACK;
    private static final int _defaultOffset = 2;
    private final ShadowBorder _shadowborder;
    @Override
    public Color getShadowColor() {
        return _shadowborder.getShadowColor();
    }
    @Override
    public void setShadowColor(Color shadow) {
        _shadowborder.setShadowColor(shadow);
    }
    @Override
    public int getOffsetX() {
        return _shadowborder.getOffsetX();
    }
    @Override
    public void setOffsetX(int offsetX) {
        _shadowborder.setOffsetX(offsetX);
    }
    @Override
    public int getOffsetY() {
        return _shadowborder.getOffsetY();
    }
    @Override
    public void setOffsetY(int offsetY) {
        _shadowborder.setOffsetY(offsetY);
    }
    public ShadowButton() {
        this(_defaultShadowColor, _defaultOffset, _defaultOffset);
    }
    public ShadowButton(Color shadow) {
        this(shadow, _defaultOffset, _defaultOffset);
    }
    public ShadowButton(int translate) {
        this(_defaultShadowColor, translate, translate);
    }
    public ShadowButton(int translateX, int translateY) {
        this(_defaultShadowColor, translateX, translateY);
    }
    public ShadowButton(Color shadow, int translate) {
        this(shadow, translate, translate);
    }
    public ShadowButton(Color shadow, int translateX, int translateY) {
        _shadowborder = new ShadowBorder(translateX, translateY, shadow);
        setBorder(_shadowborder);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Insets insets = getBorder().getBorderInsets(this);
        Rectangle rectangle = getBounds();
        setBounds(rectangle.x + insets.left, rectangle.y + insets.top, rectangle.width - insets.left - insets.right, rectangle.height - insets.top - insets.bottom);
        super.paintComponent(g);
        setBounds(rectangle);
    }
}
