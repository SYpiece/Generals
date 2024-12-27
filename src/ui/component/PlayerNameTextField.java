package ui.component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import resource.font.QuicksandFontManager;

public class PlayerNameTextField extends HintTextField {
    private static final String _defaultName = "Anonynous";
    public String getName() {
        String text = getText();
        if (text.equals("")) {
            return _defaultName;
        } else {
            return text;
        }
    }
    public PlayerNameTextField() {
        super(Color.LIGHT_GRAY, _defaultName);
        setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 20));
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(new Color(0x008080));
    }
}
