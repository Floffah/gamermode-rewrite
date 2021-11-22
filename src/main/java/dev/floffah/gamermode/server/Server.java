package dev.floffah.gamermode.server;

import dev.floffah.gamermode.visual.GuiWindow;
import dev.floffah.gamermode.visual.Logger;

import java.io.IOException;
import java.util.List;

public class Server {
    public static Server server;

    public Logger logger;
    public GuiWindow gui;

    public List<String> args;
    public boolean debugMode;

    public Server(String[] args) {
        Server.server = this;
        this.args = List.of(args);

        this.debugMode = this.args.contains("-debug");

        this.logger = new Logger(this);
        this.gui = GuiWindow.start(this);

        this.logger.info(String.format("Running on Java version %s on %s", System.getProperty("java.version"), System.getProperty("os.name")));
    }

    public void shutdown() throws IOException {
        this.logger.info("Goodbye!");

        this.gui.stop();
        System.exit(0);
    }
}
