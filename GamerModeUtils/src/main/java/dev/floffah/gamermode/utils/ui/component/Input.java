package dev.floffah.gamermode.utils.ui.component;

import dev.floffah.gamermode.utils.fonts.FontProvider;
import dev.floffah.gamermode.utils.ui.borders.RoundedCornerBorder;
import dev.floffah.gamermode.utils.ui.component.raw.JPlaceholderTextField;
import java.awt.*;

public class Input extends JPlaceholderTextField {

    public Input() {
        super();
        this.setFont(FontProvider.INSTANCE.getMonoFont().deriveFont(20f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(
                ((RoundedCornerBorder) getBorder()).getBorderShape(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1
                    )
            );
            g2.dispose();
        }

        super.paintComponent(g);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        this.setOpaque(false);
        this.setBorder(new RoundedCornerBorder());
    }
}
