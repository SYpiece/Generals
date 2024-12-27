package ui.component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import resource.font.QuicksandFontManager;

public class TitleLabel extends ShadowLabel {
    private static final Color _foreground = Color.WHITE, _shadow = new Color(0, 128, 128);
    public TitleLabel() {
        super(_shadow);
        setText("generals.io");
        setForeground(_foreground);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 80));
    }
}
