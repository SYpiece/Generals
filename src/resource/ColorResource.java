package resource;

import javafx.scene.paint.Color;

public final class ColorResource extends Resource {
    public static final Color
            RED = Color.rgb(255, 0, 0),
            LIGHT_BLUE = Color.rgb(67, 99, 216),
            GREEN = Color.rgb(0, 128, 0),
            TEAL = Color.rgb(0, 128, 128),
            ORANGE = Color.rgb(245, 130, 49),
            PINK = Color.rgb(240, 50, 230),
            PURPLE = Color.rgb(128, 0, 128),
            MAROON = Color.rgb(128, 0, 0),
            YELLOW = Color.rgb(176, 159, 48),
            BROWN = Color.rgb(154, 99, 36),
            BLUE = Color.rgb(0, 0, 255),
            PURPLE_BLUE = Color.rgb(72, 61, 139),
            GRAY = Color.rgb(128, 128, 128),
            LIGHT = Color.rgb(220, 220, 220);
    public static Color getBotColor() {
        return GRAY;
    }
    public static Color getColor(model.Color color) {
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0);
    }
    private ColorResource() {}
}
