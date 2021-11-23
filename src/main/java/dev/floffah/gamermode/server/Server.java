package dev.floffah.gamermode.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.floffah.gamermode.config.Config;
import dev.floffah.gamermode.events.EventEmitter;
import dev.floffah.gamermode.visual.GuiWindow;
import dev.floffah.gamermode.visual.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Server {
    public static Server server;

    public Logger logger;
    public GuiWindow gui;

    public List<String> args;
    public boolean debugMode;

    public ObjectMapper om;
    public String rootDir;
    public File configFile;
    public Config config;

    public EventEmitter events;

    public Server(String[] args) {
        Server.server = this;
        this.args = List.of(args);

        this.debugMode = this.args.contains("-debug");

        this.logger = new Logger(this);
        this.gui = GuiWindow.start(this);

        this.logger.info(String.format("Running on Java version %s on %s", System.getProperty("java.version"), System.getProperty("os.name")));
        this.events = new EventEmitter(this);

        this.om = new ObjectMapper(new YAMLFactory());
        try {
            if (this.args.contains("-usewd")) {
                this.rootDir = System.getProperty("user.dir");
            } else {
                this.rootDir = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toUri().getPath();
            }
            this.configFile = Path.of(this.rootDir, "config.yml").toFile();
            if (!configFile.exists()) {
                this.config = new Config();
                saveConfig();
            } else {
                loadConfig();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() throws IOException {
        this.config = om.readValue(this.configFile, Config.class);
    }

    public void saveConfig() throws IOException {
        om.writeValue(this.configFile, this.config);
    }

    public void fatalShutdown(Exception e) {
        this.logger.printStackTrace(e);
        try {
            this.shutdown(1);
        } catch (IOException e1) {
            this.logger.printStackTrace(e1);
            System.exit(1);
        }
    }

    public void shutdown(int status) throws IOException {
        this.logger.info("Goodbye!");

        this.gui.stop();
        System.exit(status);
    }

    public void shutdown() throws IOException {
        this.shutdown(0);
    }
}
