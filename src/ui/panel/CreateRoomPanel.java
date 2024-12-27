package ui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import model.Player;
import resource.font.QuicksandFontManager;
import ui.component.ShadowButton;
import ui.component.ShadowPanel;
import ui.frame.GameFrame;
import util.ui.UIUtil;

public class CreateRoomPanel extends JPanel {
    private static final Dimension _createRoomPanelSize = new Dimension(900, 500);
    public CreateRoomPanel() {
        setLayout(null);
        setBackground(new Color(34, 34, 34));
        JPanel realPanel = new JPanel(null);
        realPanel.setBackground(new Color(34, 34, 34));
        realPanel.setSize(_createRoomPanelSize);
        {
            JPanel hostPanel = new JPanel(null);
            hostPanel.setBackground(new Color(34, 34, 34));
            hostPanel.setBounds((realPanel.getWidth() - 500) / 2, 0, 500, 70);
            {
                JLabel ipLabel = new JLabel("IP:");
                ipLabel.setBounds(0, 0, 50, 30);
                ipLabel.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 15));
                ipLabel.setForeground(Color.WHITE);
                hostPanel.add(ipLabel);

                JTextField ipTextField = new JTextField("127.0.0.1");
                ipTextField.setEditable(false);
                ipTextField.setBounds(50, 0, 450, 30);
                ipTextField.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 15));
                ipTextField.setForeground(new Color(0x008080));
                ipTextField.setBackground(Color.WHITE);
                ipTextField.setBorder(null);
                hostPanel.add(ipTextField);

                JLabel portLabel = new JLabel("Port:");
                portLabel.setBounds(0, 40, 50, 30);
                portLabel.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 15));
                portLabel.setForeground(Color.WHITE);
                hostPanel.add(portLabel);

                JTextField portTextField = new JTextField("8080");
                portTextField.setEditable(false);
                portTextField.setBounds(50, 40, 450, 30);
                portTextField.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 15));
                portTextField.setForeground(new Color(0x008080));
                portTextField.setBackground(Color.WHITE);
                portTextField.setBorder(null);
                hostPanel.add(portTextField);
            }
            realPanel.add(hostPanel);

            JPanel teamPanel = new JPanel(null);
            teamPanel.setBackground(new Color(51, 51, 51));
            teamPanel.setBounds(0, 100, realPanel.getWidth(), 300);
            int teamCount = 12;
            {
                JLabel teamSelectLabel = new JLabel("Select your Team:", SwingConstants.CENTER);
                teamSelectLabel.setBounds(0, 0, teamPanel.getWidth(), 40);
                teamSelectLabel.setForeground(Color.WHITE);
                teamSelectLabel.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 20));
                teamPanel.add(teamSelectLabel);

                JPanel teamSelectButtonPanel = new ShadowPanel(new Color(0x008080), 2);
                teamSelectButtonPanel.setLayout(null);
                int teamSelectButtonPanelWidth = 60 * (teamCount + 1) + 2;
                teamSelectButtonPanel.setBounds((teamPanel.getWidth() - teamSelectButtonPanelWidth) / 2, 40, teamSelectButtonPanelWidth, 40 + 2);
                {
                    ButtonGroup teamButtonGroup = new ButtonGroup();
                    for (int i = 1; i <= teamCount; i++) {
                        JToggleButton button = new JToggleButton(String.valueOf(i));
                        button.setBounds(60 * (i - 1), 0, 60, 40);
                        button.setForeground(Color.BLACK);
                        button.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
                        button.setBackground(Color.WHITE);
                        button.setBorder(null);
                        teamButtonGroup.add(button);
                        teamSelectButtonPanel.add(button);
                    }
                    JToggleButton spectatorTeamButton = new JToggleButton("Spectator");
                    spectatorTeamButton.setMargin(new Insets(0, 0, 0, 0));
                    spectatorTeamButton.setBounds(teamCount * 60, 0, 60, 40);
                    spectatorTeamButton.setForeground(Color.BLACK);
                    spectatorTeamButton.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 10));
                    spectatorTeamButton.setBackground(Color.WHITE);
                    spectatorTeamButton.setBorder(null);
                    teamButtonGroup.add(spectatorTeamButton);
                    teamSelectButtonPanel.add(spectatorTeamButton);
                }
                teamPanel.add(teamSelectButtonPanel);

                JPanel recentTeamPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                recentTeamPanel.setBackground(new Color(51, 51, 51));
                {
                    List<TeamPanel> recentTeamPanelList = new ArrayList<>(teamCount + 1);
                    for (int i = 1; i <= teamCount; i++) {
                        TeamPanel panel = new TeamPanel(teamCount);
                        recentTeamPanelList.add(panel);
                    }
                }
                realPanel.add(recentTeamPanel);
            }
            realPanel.add(teamPanel);

            JButton cancelButton = new ShadowButton(new Color(0x008080), 2);
            cancelButton.setText("Cancel");
            cancelButton.setBounds((realPanel.getWidth() - 120) / 2, 450, 120, 40);
            cancelButton.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 20));
            cancelButton.setForeground(new Color(0x008080));
            cancelButton.setBackground(Color.WHITE);
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameFrame.gameFrame.popPanel();
                }
            });
            realPanel.add(cancelButton);
        }
        add(realPanel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                UIUtil.setComponentBoundsAtContainerCentre(realPanel, CreateRoomPanel.this);
            }
        });
    }
    protected static class TeamPanel extends JPanel {
        private final Map<Player, JLabel> _players = new TreeMap<>();
        private final int _team;
        public int getTeam() {
            return _team;
        }
        public void addPlayer(Player player) {
            JLabel playerLabel = new JLabel(player.getName(), SwingConstants.CENTER);
            playerLabel.setForeground(Color.WHITE);
            playerLabel.setFont(QuicksandFontManager.quicksandFont(Font.PLAIN, 15));
            add(playerLabel);
            _players.put(player, playerLabel);
        }
        public void removePlayer(Player player) {
            remove(_players.get(player));
            _players.remove(player);
        }
        public Player[] getPlayers() {
            return _players.keySet().toArray(new Player[_players.size()]);
        }
        public TeamPanel(int team) {
            _team = team;
            JLabel teamLabel = new JLabel("Team " + team, SwingConstants.CENTER);
            teamLabel.setForeground(Color.WHITE);
            teamLabel.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 20));
            add(teamLabel);
        }
    }
}
