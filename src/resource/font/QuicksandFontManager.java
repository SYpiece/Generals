package resource.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public final class QuicksandFontManager {
    private static Font quicksandFont;
    static {
        try (InputStream inputStream = QuicksandFontManager.class.getResource("/resource/font/Quicksand.otf").openStream()) {
            quicksandFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
    private QuicksandFontManager() {}
    public static Font quicksandFont() {
        return quicksandFont;
    }
    public static Font quicksandFont(int style) {
        return quicksandFont.deriveFont(style);
    }
    public static Font quicksandFont(float fontSize) {
        return quicksandFont.deriveFont(fontSize);
    }
    public static Font quicksandFont(int style, float fontSize) {
        return quicksandFont.deriveFont(style, fontSize);
    }
}
