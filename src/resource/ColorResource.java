package resource;

import javafx.scene.paint.Color;

public final class ColorResource {
    public static final Color RED = Color.rgb(255, 0, 0);
    public static final Color LIGHT_BLUE = Color.rgb(67, 99, 216);
    public static final Color GREEN = Color.rgb(0, 128, 0);
    public static final Color TEAL = Color.rgb(0, 128, 128);
    public static final Color ORANGE = Color.rgb(245, 130, 49);
    public static final Color PINK = Color.rgb(240, 50, 230);
    public static final Color PURPLE = Color.rgb(128, 0, 128);
    public static final Color MAROON = Color.rgb(128, 0, 0);
    public static final Color YELLOW = Color.rgb(176, 159, 48);
    public static final Color BROWN = Color.rgb(154, 99, 36);
    public static final Color BLUE = Color.rgb(0, 0, 255);
    public static final Color PURPLE_BLUE = Color.rgb(72, 61, 139);
    public static final Color GRAY = Color.rgb(128, 128, 128);
    public static final Color LIGHT = Color.rgb(220, 220, 220);
    public static Color getBotColor() {
        return GRAY;
    }
    public static Color getColor(resource.color.Color color) {
        return Color.rgb(color.getRgb(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0);
    }
    private ColorResource() {}
}
