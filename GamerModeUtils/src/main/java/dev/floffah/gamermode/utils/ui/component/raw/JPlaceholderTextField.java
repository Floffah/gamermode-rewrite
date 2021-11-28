package dev.floffah.gamermode.utils.ui.component.raw;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

// see https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder

public class JPlaceholderTextField extends JTextField {

    @Getter
    @Setter
    private String placeholder;

    public JPlaceholderTextField() {
        super();
    }

    public JPlaceholderTextField(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (
            placeholder == null ||
            placeholder.length() == 0 ||
            getText().length() > 0
        ) return;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setColor(getDisabledTextColor());
        g2d.drawString(
            placeholder,
            getInsets().left,
            g2d.getFontMetrics().getMaxAscent() + getInsets().top
        );
    }
}
