package util.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;

public class UIUtil {
    private UIUtil() {}
    public static void setWindowBoundsAtScreenCentre(Window window, Dimension size) {
        Insets insets = window.getInsets();
        Dimension dimension = window.getToolkit().getScreenSize();
        window.setBounds((dimension.width - size.width - insets.left - insets.right) / 2, (dimension.height - size.height - insets.top - insets.bottom) / 2, size.width + insets.left + insets.right, size.height + insets.top + insets.bottom);
    }
    public static void setComponentBoundsAtContainerCentre(Component component, Container container) {
        component.setLocation((container.getWidth() - component.getWidth()) / 2, (container.getHeight() - component.getHeight()) / 2);
    }
}
