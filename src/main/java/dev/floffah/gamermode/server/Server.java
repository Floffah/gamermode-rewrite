package dev.floffah.gamermode.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.floffah.gamermode.config.Config;
import dev.floffah.gamermode.events.EventEmitter;
import dev.floffah.gamermode.server.cache.CacheProvider;
import dev.floffah.gamermode.server.socket.SocketManager;
import dev.floffah.gamermode.visual.GuiWindow;
import dev.floffah.gamermode.visual.Logger;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;

public class Server {

    public static Server server;

    /**
     * The server's logger
     * -- GETTER --
     * Get the server's logger
     *
     * @return The server's logger
     */
    @Getter
    protected Logger logger;

    /**
     * The GUI window
     * -- GETTER --
     * Get the GUI window
     *
     * @return The GUI window
     */
    @Getter
    protected GuiWindow gui;

    /**
     * The process arguments
     * -- GETTER --
     * Get the process arguments
     *
     * @return The process arguments
     */
    @Getter
    protected List<String> args;

    /**
     * Whether the server is in debug mode or not
     * -- GETTER --
     * Get whether the server is in debug mode or not
     *
     * @return Whether the server is in debug mode or not
     */
    @Getter
    protected boolean debugMode;

    /**
     * The server's configuration
     * -- GETTER --
     * Get the server's configuration
     *
     * @return The server's configuration
     */
    @Getter
    protected Config config;

    /**
     * The server's running/root directory
     * -- GETTER --
     * Get the server's running/root directory
     *
     * @return The server's running/root directory
     */
    @Getter
    protected String rootDir;

    /**
     * The directory the server stores its data in
     * -- GETTER --
     * Get the directory the server stores its data in
     *
     * @return The directory the server stores its data in
     */
    @Getter
    protected Path dataDir;

    /**
     * The server's event emitter
     * -- GETTER --
     * Get the server's event emitter
     *
     * @return The server's event emitter
     */
    @Getter
    protected EventEmitter events;

    /**
     * The server's cache provider
     * -- GETTER --
     * Get the server's cache provider
     *
     * @return The server's cache provider
     */
    @Getter
    protected CacheProvider cache;

    /**
     * The server's main thread pool
     * -- GETTER --
     * Get the server's main thread pool
     *
     * @return The server's main thread pool
     */
    @Getter
    protected ExecutorService pool;

    /**
     * The server's scheduled thread pool
     * -- GETTER --
     * Get the server's scheduled thread pool
     *
     * @return The server's scheduled thread pool
     */
    @Getter
    protected ScheduledExecutorService scheduler;

    /**
     * The server's small task thread pool.
     * Only use the task pool for quick and short tasks. For anything else, use the thread pool or the scheduled pool.
     * -- GETTER --
     * Get the server's small task thread pool.
     * Only use the task pool for quick and short tasks. For anything else, use the thread pool or the scheduled pool.
     *
     * @return The server's small task thread pool.
     */
    @Getter
    protected ExecutorService taskPool;

    /**
     * The server's key pair generator
     * -- GETTER --
     * Get the server's key pair generator
     *
     * @return The server's key pair generator
     */
    @Getter
    protected KeyPairGenerator keyPairGenerator;

    /**
     * The server's socket manager
     * -- GETTER --
     * Get the server's socket manager
     *
     * @return The server's socket manager
     */
    @Getter
    protected SocketManager sock;

    /**
     * The server's protocol version
     * -- GETTER --
     * Get the server's protocol version
     *
     * @return The server's protocol version
     */
    @Getter
    protected int protocolVersion = 756;

    /**
     * The server's id
     * -- GETTER --
     * Get the server's id
     *
     * @return The server's id
     */
    @Getter
    protected String serverId = "GamerModeServer";

    /**
     * The server's brand
     * -- GETTER --
     * Get the server's brand
     *
     * @return The server's brand
     */
    @Getter
    protected String serverBrand = "GamerMode";

    ObjectMapper om;
    File configFile;

    /**
     * initialise a new server instance
     *
     * @param args the command line arguments
     */
    public Server(String[] args) {
        Server.server = this;
        this.args = List.of(args);

        // debug
        this.debugMode = this.args.contains("-debug");

        // output
        this.logger = new Logger(this);
        this.gui = GuiWindow.start(this);

        // info
        this.getLogger()
            .info(
                String.format(
                    "Running on Java version %s on %s",
                    System.getProperty("java.version"),
                    System.getProperty("os.name")
                )
            );

        // events
        this.events = new EventEmitter(this);

        // config
        this.om = new ObjectMapper(new YAMLFactory());
        try {
            // get root dir
            if (this.args.contains("-usewd")) {
                this.rootDir = System.getProperty("user.dir");
            } else {
                this.rootDir =
                    Paths
                        .get(
                            getClass()
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI()
                        )
                        .getParent()
                        .toUri()
                        .getPath();
            }
            // create or load config
            this.configFile = Path.of(this.rootDir, "config.yml").toFile();
            if (!configFile.exists()) {
                this.config = new Config();
                saveConfig();
            } else {
                loadConfig();
            }
        } catch (URISyntaxException | IOException e) {
            this.fatalShutdown(e);
        }

        // directories
        this.dataDir = Path.of(this.rootDir, "data");
        dataDir.toFile().mkdirs();

        // threading
        this.pool =
            Executors.newFixedThreadPool(this.config.performance.poolSize);
        this.scheduler =
            Executors.newScheduledThreadPool(
                this.config.performance.scheduledPoolSize
            );
        this.taskPool = Executors.newCachedThreadPool();

        // cache
        this.cache = new CacheProvider(this);

        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            this.keyPairGenerator.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            this.fatalShutdown(e);
        }

        this.getLogger().info("Server initialised. Starting socket...");

        this.sock = new SocketManager(this);
        try {
            this.sock.start();
        } catch (IOException e) {
            this.fatalShutdown(e);
        }
    }

    /**
     * Read and load the config
     *
     * @throws IOException An exception from Jackson's ObjectMapper
     */
    public void loadConfig() throws IOException {
        this.config = om.readValue(this.configFile, Config.class);
    }

    /**
     * Save the config
     *
     * @throws IOException any exception from Jackson's ObjectMapper
     */
    public void saveConfig() throws IOException {
        om.writeValue(this.configFile, this.config);
    }

    /**
     * Shut down the server when a fatal error occurs and log it
     *
     * @param e The fatal error/exception
     */
    public void fatalShutdown(Exception e) {
        this.getLogger().printStackTrace(e);
        try {
            this.shutdown(1);
        } catch (IOException e1) {
            this.getLogger().printStackTrace(e1);
            System.exit(1);
        }
    }

    /**
     * Shut down the server with a specific exit code
     *
     * @param status the exit code
     * @throws IOException any exception from the socket manager
     */
    public void shutdown(int status) throws IOException {
        this.getLogger().info("Goodbye!");

        this.gui.stop();
        System.exit(status);
    }

    /**
     * Shut down the server with exit code 0
     *
     * @throws IOException any exception from the socket manager
     */
    public void shutdown() throws IOException {
        this.shutdown(0);
    }
}
