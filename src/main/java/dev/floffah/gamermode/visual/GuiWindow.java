package dev.floffah.gamermode.visual;

import dev.floffah.gamermode.server.Server;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;

public class GuiWindow {
    public boolean loaded = false;
    Server main;
    JFrame frame;
    JTextArea text;

    public GuiWindow(Server main) {
        this.main = main;
    }

    public static GuiWindow start(Server main) {
        boolean isDoubleClick = check();
        if ((isDoubleClick || (main.args.contains("-gui")) && !main.args.contains("-nogui"))) {
            try {
                return create(main);
            } catch (IOException e) {
                main.logger.printStackTrace(e);
            }
        }
        return null;
    }

    public static boolean check() {
        Console console = System.console();
        return console == null;
    }

    public static GuiWindow create(Server main) throws IOException {
        GuiWindow win = new GuiWindow(main);
        win.startOutput();
        return win;
    }

    public void startOutput() throws IOException {
        this.frame = new JFrame("GamerMode Output");

        text = new JTextArea();
        text.setEditable(false);
        text.setText("");
        text.setAutoscrolls(true);
        text.setRows(5);
        text.setColumns(50);
        DefaultCaret c = (DefaultCaret) text.getCaret();
        c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Font font;
        try {
            InputStream fonts = getClass().getClassLoader().getResourceAsStream("JetBrainsMono-Regular.ttf");
            if (fonts == null) {
                main.logger.info("Does not exist");
                main.shutdown();
                return;
            }
            font = Font.createFont(Font.TRUETYPE_FONT, fonts);
        } catch (FontFormatException | IOException | NullPointerException e) {
            main.logger.printStackTrace(e);
            main.shutdown();
            return;
        }

        text.setFont(font.deriveFont(12f));
        text.setBackground(new Color(15, 15, 15));
        text.setForeground(new Color(204, 204, 204));

        frame.add(new JScrollPane(text));
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    main.shutdown();
                } catch (IOException ioException) {
                    main.logger.printStackTrace(ioException);
                }
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

        loaded = true;
    }

    public void stop() {
        frame.setVisible(false);
        frame = null;
    }

    public void log(String message) {
        text.append(message);
    }
}