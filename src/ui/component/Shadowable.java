package ui.component;

import java.awt.Color;

public interface Shadowable {
    Color getShadowColor();
    void setShadowColor(Color shadow);
    int getOffsetX();
    void setOffsetX(int translateX);
    int getOffsetY();
    void setOffsetY(int translateY);
}
