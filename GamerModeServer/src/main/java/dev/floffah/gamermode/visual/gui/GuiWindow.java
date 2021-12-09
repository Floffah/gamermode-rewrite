package dev.floffah.gamermode.visual.gui;

import dev.floffah.gamermode.events.EventListener;
import dev.floffah.gamermode.events.Listener;
import dev.floffah.gamermode.events.state.ServerLoadEvent;
import dev.floffah.gamermode.server.Server;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class GuiWindow implements Listener {

    public boolean loaded = false;
    Server main;
    JFrame frame;
    JTextArea text;

    /**
     * Instantiates a new GUI window.
     *
     * @param main The main server instance.
     */
    public GuiWindow(Server main) {
        this.main = main;
        this.main.getEvents().registerListeners(this);
    }

    /**
     * Start a new GUI window instance if it is possible
     *
     * @param main The main server instance.
     * @return The new instance of the GUI window.
     */
    public static GuiWindow start(Server main) {
        boolean isDoubleClick = check();
        if (
            (
                isDoubleClick ||
                (main.getArgs().contains("-gui")) &&
                !main.getArgs().contains("-nogui")
            )
        ) {
            try {
                return create(main);
            } catch (IOException e) {
                main
                    .getLogger()
                    .error("Error occurred while creating the gui window", e);
            }
        }
        return null;
    }

    /**
     * Check if the user double-clicked the file.
     *
     * @return True if the user double-clicked the file.
     */
    public static boolean check() {
        Console console = System.console();
        return console == null;
    }

    /**
     * Util method to create a new GUI window instance properly.
     *
     * @param main The main server instance.
     * @return The new instance of the GUI window.
     * @throws IOException any error from the shutdown process.
     */
    public static GuiWindow create(Server main) throws IOException {
        GuiWindow win = new GuiWindow(main);
        win.startOutput();
        return win;
    }

    /**
     * Creates the GUI window and starts the output.
     *
     * @throws IOException any error from the shutdown process.
     */
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
            InputStream fonts = getClass()
                .getClassLoader()
                .getResourceAsStream("JetBrainsMono-Regular.ttf");
            if (fonts == null) {
                main.getLogger().info("Does not exist");
                main.shutdown();
                return;
            }
            font = Font.createFont(Font.TRUETYPE_FONT, fonts);
        } catch (FontFormatException | IOException | NullPointerException e) {
            main.fatalShutdown(e);
            return;
        }

        text.setFont(font.deriveFont(12f));
        text.setBackground(new Color(15, 15, 15));
        text.setForeground(new Color(204, 204, 204));

        frame.add(new JScrollPane(text));
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    try {
                        main.shutdown();
                    } catch (IOException ioException) {
                        main
                            .getLogger()
                            .error(
                                "Error occurred while trying to shutdown the server after gui window was closed by user",
                                ioException
                            );
                    }
                }
            }
        );
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

        loaded = true;
    }

    @EventListener
    public void onLoaded(ServerLoadEvent e) {
        this.main.getDaemonPool()
            .execute(() -> {
                String nextLog;
                while (
                    (nextLog = GuiAppender.next("GuiWindow")) !=
                    null
                ) {
                    this.log(nextLog);
                }
            });
    }

    /**
     * Stop and close the GUI window.
     */
    public void stop() {
        frame.setVisible(false);
        frame = null;
    }

    /**
     * Append a new line to the GUI window.
     *
     * @param message The message to append.
     */
    public void log(String message) {
        if (SwingUtilities.isEventDispatchThread()) this.text.append(
                message
            ); else SwingUtilities.invokeLater(() -> this.log(message));
    }
}
