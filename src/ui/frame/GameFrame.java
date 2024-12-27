package ui.frame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ui.panel.MainPanel;
import util.ui.UIUtil;

public class GameFrame extends JFrame {
    public final static GameFrame gameFrame;
    private static final Dimension _gameFrameSize;
    static {
        gameFrame = new GameFrame();
        _gameFrameSize = new Dimension(1000, 600);
    }
    private final List<JPanel> _panels = new LinkedList<>();
    private final JLayeredPane _layeredPane = new JLayeredPane();
    private final MainPanel _mainPanel = new MainPanel();
    private GameFrame() {
        super("generals.io");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                UIUtil.setWindowBoundsAtScreenCentre(GameFrame.this, _gameFrameSize);
                setMinimumSize(_gameFrameSize);
                initComponents();
                pushPanel(_mainPanel);
            }
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    private void initComponents() {
        _layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component component : _layeredPane.getComponents()) {
                    component.setSize(_layeredPane.getSize());
                }
            }
        });
        add(_layeredPane);
    }
    public void pushPanel(JPanel panel) {
        panel.setBounds(0, 0, _layeredPane.getWidth(), _layeredPane.getHeight());
        _layeredPane.add(panel, Integer.valueOf(_panels.size()));
        _panels.add(panel);
        _layeredPane.revalidate();
    }
    public void popPanel() {
        JPanel panel = _panels.get(_panels.size() - 1);
        _layeredPane.remove(panel);
        _panels.remove(panel);
        for (int i = _panels.size() - 1; i >= 0 && _panels.get(i) == null; i--) {
            _panels.remove(i);
        }
        _layeredPane.revalidate();
        _layeredPane.repaint();
        if (_panels.isEmpty()) {
            dispose();
        }
    }
    public void removePanel(JPanel panel) {
        if (panel == _panels.get(_panels.size() - 1)) {
            popPanel();
        } else {
            _panels.set(_panels.indexOf(panel), null);
            _layeredPane.remove(panel);
        }
    }
}
