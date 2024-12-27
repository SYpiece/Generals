package ui.component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class HintTextField extends JTextField {
    private static final Color _defaultHintColor = Color.LIGHT_GRAY;
    private String _hint;
    private boolean _hasText;
    private Color _hintColor, _foregroundColor;
    public String getHint() {
        return _hint;
    }
    public void setHint(String hint) {
        _hint = hint;
    }
    public Color getHintColor() {
        return _hintColor;
    }
    public void setHintColor(Color hintColor) {
        _hintColor = hintColor;
    }
    @Override
    public String getText() {
        if (_hasText) {
            return super.getText();
        } else {
            return "";
        }
    }
    @Override
    public void setText(String text) {
        if (text.equals("")) {
            super.setForeground(_hintColor);
            super.setText(_hint);
            _hasText = false;
        } else {
            super.setForeground(_foregroundColor);
            super.setText(text);
            _hasText = true;
        }
    }
    @Override
    public void setForeground(Color fg) {
        _foregroundColor = fg;
        if (_hasText) {
            super.setForeground(_foregroundColor);
        } else {
            super.setForeground(_hintColor);
        }
    }
    public HintTextField() {
        this("");
    }
    public HintTextField(String hint) {
        this(_defaultHintColor, hint);
    }
    public HintTextField(Color hintColor, String hint) {
        setBorder(BorderFactory.createEmptyBorder());
        setHintColor(hintColor);
        setHint(hint);
        setText("");
        _foregroundColor = getForeground();
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                HintTextField.this.focusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                HintTextField.this.focusLost(e);
            }
        });
    }
    private void focusGained(FocusEvent e) {
        if (!_hasText) {
            super.setForeground(_foregroundColor);
            super.setText("");
            _hasText = true;
        }
    }
    private void focusLost(FocusEvent e) {
        String text = getText();
        setText(text);
    }
}
