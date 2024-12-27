package ui.component;

import java.awt.Color;
import java.awt.Font;

import resource.font.QuicksandFontManager;

public class PlayButton extends ShadowButton {
    private static final Color _defaultShadowColor = new Color(0x008080);
    public PlayButton() {
        super(_defaultShadowColor);
        setText("Play");
        setForeground(new Color(0x008080));
        setBackground(Color.WHITE);
        setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 40));
    }
}
