package ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import ui.component.PlayButton;
import ui.component.PlayerNameTextField;
import ui.component.TitleLabel;
import ui.component.VersionPanel;
import ui.frame.GameFrame;
import util.ui.UIUtil;

public class MainPanel extends JPanel {
    private static final Dimension _mainPanelSize = new Dimension(800, 500);
    public MainPanel() {
        initComponents();
    }
    private void initComponents() {
        setBackground(new Color(0x222222));
        setLayout(null);
        {
            JPanel realPanel = new JPanel(null);
            realPanel.setSize(_mainPanelSize);
            realPanel.setOpaque(false);
            {
                TitleLabel titleLabel = new TitleLabel();
                titleLabel.setBounds(0, 100, 800, 100);
                realPanel.add(titleLabel);
                PlayerNameTextField nameTextField = new PlayerNameTextField();
                nameTextField.setBounds(200, 240, 400, 40);
                nameTextField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {}
                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                });
                realPanel.add(nameTextField);
                PlayButton playButton = new PlayButton();
                playButton.setBounds(300, 300, 200, 70);
                playButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameFrame.gameFrame.pushPanel(new RoomPanel());
                    }
                });
                realPanel.add(playButton);
            }
            add(realPanel);
            VersionPanel versionPanel = new VersionPanel();
            versionPanel.setOpaque(false);
            add(versionPanel);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    UIUtil.setComponentBoundsAtContainerCentre(realPanel, MainPanel.this);
                    versionPanel.setLocation(getWidth() - versionPanel.getWidth(), getHeight() - versionPanel.getHeight());
                }
            });
        }
    }
}