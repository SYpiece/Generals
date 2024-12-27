package ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import resource.font.QuicksandFontManager;
import resource.version.VersionResource;

public class VersionPanel extends JPanel {
    public VersionPanel() {
        setLayout(new GridLayout(2, 1, 0, 0));
        JLabel gameVersionLabel = new JLabel("game version: " + VersionResource.gameVersion, SwingConstants.RIGHT), coreVersionLabel = new JLabel("core version: " + VersionResource.coreVersion, SwingConstants.RIGHT);
        gameVersionLabel.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
        gameVersionLabel.setForeground(Color.WHITE);
        coreVersionLabel.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
        coreVersionLabel.setForeground(Color.WHITE);
        add(gameVersionLabel);
        add(coreVersionLabel);
        setSize(getPreferredSize());
    }
}
