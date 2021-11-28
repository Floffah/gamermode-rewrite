package dev.floffah.gamermode.utils.window;

import dev.floffah.gamermode.utils.fonts.FontProvider;
import dev.floffah.gamermode.utils.ui.UUIDConvertPane;
import lombok.Getter;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class UtilWindow {

    public List<String> args;

    JFrame frame;
    UUIDConvertPane uuidConvert;

    @Getter
    private FontProvider fonts;

    public UtilWindow(List<String> args) {
        this.args = args;
    }

    public void start() {
        this.fonts = new FontProvider();

        this.frame = new JFrame("GamerMode Utils");

        //        this.frame.setLayout(new BorderLayout());
        this.frame.setResizable(true);
        this.frame.setMinimumSize(new Dimension(700, 500));
        this.frame.setDefaultCloseOperation(
            WindowConstants.DO_NOTHING_ON_CLOSE
        );

        val winref = this;
        this.frame.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    winref.shutdown();
                }
            }
        );

        this.uuidConvert = new UUIDConvertPane();
        this.frame.add(this.uuidConvert);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void shutdown() {
        this.frame.setVisible(false);
        this.frame = null;
        System.exit(0);
    }
}
