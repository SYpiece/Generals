package ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import resource.font.QuicksandFontManager;
import ui.component.ShadowButton;
import ui.component.ShadowPanel;
import ui.frame.GameFrame;
import util.ui.UIUtil;

public class RoomPanel extends JPanel {
    private static final Dimension _roomPanelSize = new Dimension(400, 190);
    public RoomPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 128));
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GameFrame.gameFrame.removePanel(RoomPanel.this);
            }
        });
        ShadowPanel realPanel = new ShadowPanel(new Color(0x008080), 2);
        realPanel.setLayout(null);
        realPanel.setBackground(Color.WHITE);
        realPanel.setSize(_roomPanelSize);
        {
            JButton joinButton = new ShadowButton(Color.BLACK, 2);
            joinButton.setText("Join");
            joinButton.setForeground(Color.WHITE);
            joinButton.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 25));
            joinButton.setBackground(new Color(0x008080));
            joinButton.setBounds(100, 30, 200, 40);
            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameFrame.gameFrame.removePanel(RoomPanel.this);
                    GameFrame.gameFrame.pushPanel(new JoinRoomPanel());
                }
            });
            realPanel.add(joinButton);

            JLabel joinLabel = new JLabel("Play in the same room with some friends.", SwingConstants.CENTER);
            joinLabel.setForeground(Color.BLACK);
            joinLabel.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
            joinLabel.setBounds(0, 70, 400, 20);
            realPanel.add(joinLabel);

            JButton createButton = new ShadowButton(Color.BLACK, 2);
            createButton.setText("Create");
            createButton.setForeground(Color.WHITE);
            createButton.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 25));
            createButton.setBackground(new Color(0x008080));
            createButton.setBounds(100, 100, 200, 40);
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameFrame.gameFrame.removePanel(RoomPanel.this);
                    GameFrame.gameFrame.pushPanel(new CreateRoomPanel());
                }
            });
            realPanel.add(createButton);

            JLabel createLabel = new JLabel("Create a room and wait for someone.", SwingConstants.CENTER);
            createLabel.setForeground(Color.BLACK);
            createLabel.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
            createLabel.setBounds(0, 140, 400, 20);
            realPanel.add(createLabel);
        }
        add(realPanel);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                UIUtil.setComponentBoundsAtContainerCentre(realPanel, RoomPanel.this);
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
