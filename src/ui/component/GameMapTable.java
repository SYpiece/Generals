package ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import model.Block;
import model.Map;
import resource.ColorResource;
import resource.font.QuicksandFontManager;

public class GameMapTable extends JTable {
    private final GameMapTableModel _gameMapTableModel;
    private final GameMapTableCellRenderer _gameMapTableCellRenderer = new GameMapTableCellRenderer();
    public final static int blockMinWidthAndHeight = 15, blockMaxWidthAndHeight = 250;
    public final static int dragMoveDistance = 10;
    private Point _clickPoint = new Point(-1, -1);
    public GameMapTable(Map map) {
        _gameMapTableModel = new GameMapTableModel(map);
        setModel(_gameMapTableModel);
        setDefaultRenderer(Block.class, _gameMapTableCellRenderer);
        setIntercellSpacing(new Dimension(0, 0));
        setRowHeight(30);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point _mousePoint;
            private boolean _isDraging;
            @Override
            public void mousePressed(MouseEvent e) {
                _mousePoint = e.getPoint();
                _isDraging = false;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!_isDraging) {
                    _clickPoint = new Point(columnAtPoint(_mousePoint), rowAtPoint(_mousePoint));
                    repaint();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getPoint().distance(_mousePoint) > dragMoveDistance) {
                    _isDraging = true;
                    _clickPoint = new Point(-1, -1);
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
    protected static class GameMapTableModel extends AbstractTableModel {
        private final Map _map;
        public GameMapTableModel(Map map) {
            _map = map;
        }
        public Map getMap() {
            return _map;
        }
        @Override
        public int getRowCount() {
            return _map.getWidth();
        }
        @Override
        public int getColumnCount() {
            return _map.getHeight();
        }
        @Override
        public Class<Block> getColumnClass(int columnIndex) {
            return Block.class;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return _map.getBlockAt(rowIndex, columnIndex);
        }
    }
    protected class GameMapTableCellRenderer extends JPanel implements TableCellRenderer {
        private boolean _hasShadow, _hasFound;
        private final JLabel _peopleLable = new JLabel(), _leftOrderLable = new JLabel("←", SwingConstants.LEFT), _rightOrderLabel = new JLabel("→", SwingConstants.RIGHT), _upOrderLabel = new JLabel("↑", SwingConstants.CENTER), _downOrderLabel = new JLabel("↓", SwingConstants.CENTER);
        private final Border _blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1), _whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
        private Image _image;
        public GameMapTableCellRenderer() {
            super(null);
            setBackground(Color.WHITE);
            _peopleLable.setHorizontalAlignment(SwingConstants.CENTER);
            _upOrderLabel.setVerticalAlignment(SwingConstants.TOP);
            _downOrderLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            _peopleLable.setForeground(Color.WHITE);
            _leftOrderLable.setForeground(Color.WHITE);
            _rightOrderLabel.setForeground(Color.WHITE);
            _upOrderLabel.setForeground(Color.WHITE);
            _downOrderLabel.setForeground(Color.WHITE);
            _peopleLable.setFont(QuicksandFontManager.quicksandFont(Font.BOLD, 13));
            add(_peopleLable);
            add(_leftOrderLable);
            add(_rightOrderLabel);
            add(_upOrderLabel);
            add(_downOrderLabel);
        }
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            _peopleLable.setBounds(0, 0, getWidth(), getHeight());
            _leftOrderLable.setBounds(0, 0, getWidth(), getHeight());
            _rightOrderLabel.setBounds(0, 0, getWidth(), getHeight());
            _upOrderLabel.setBounds(0, 0, getWidth(), getHeight());
            _downOrderLabel.setBounds(0, 0, getWidth(), getHeight());
        }
        public void updateBlock(Block block, int distanceToClickPoint, boolean hasFound, boolean hasLeftOrder, boolean hasRightOrder, boolean hasUpOrder, boolean hasDownOrder) {
            _hasShadow = distanceToClickPoint == 1;
            _hasFound = hasFound;
            _image = block.getImage(hasFound);
            if (hasFound && block.getPeople() > 0) {
                _peopleLable.setText(String.valueOf(block.getPeople()));
            } else {
                _peopleLable.setText("");
            }
            setBorder(distanceToClickPoint == 0 ? _whiteBorder : _blackBorder);
            setBackground(block.getColor());
            _leftOrderLable.setVisible(hasLeftOrder);
            _rightOrderLabel.setVisible(hasRightOrder);
            _upOrderLabel.setVisible(hasUpOrder);
            _downOrderLabel.setVisible(hasDownOrder);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            Insets insets = getInsets();
            g.drawImage(_image, insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom, this);
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (_hasShadow) {
                g.setColor(new Color(0, 0, 0, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            if (!_hasFound) {
                g.setColor(new Color(0, 0, 0, 26));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            updateBlock((Block) value, Math.abs(_clickPoint.y - row) + Math.abs(_clickPoint.x - column), true, false, false, false, false);
            return this;
        }
        @Override
        public void invalidate() {}
        @Override
        public boolean isOpaque() { return true; }
        @Override
        public void repaint() {}
        @Override
        public void repaint(Rectangle r) {}
        @Override
        public void repaint(long tm) {}
        @Override
        public void repaint(long tm, int x, int y, int width, int height) {}
        @Override
        public void repaint(int x, int y, int width, int height) {}
        @Override
        public void revalidate() {}
        @Override
        public void validate() {}
        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
        @Override
        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
        @Override
        public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
        @Override
        public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
        @Override
        public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
        @Override
        public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
        @Override
        public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
        @Override
        public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
        @Override
        public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
        @Override
        protected void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {}
    }
}