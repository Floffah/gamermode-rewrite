package dev.floffah.gamermode.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.floffah.gamermode.GamerMode;
import dev.floffah.gamermode.command.CommandManager;
import dev.floffah.gamermode.config.Config;
import dev.floffah.gamermode.console.Console;
import dev.floffah.gamermode.entity.player.Player;
import dev.floffah.gamermode.events.EventEmitter;
import dev.floffah.gamermode.events.state.ServerLoadEvent;
import dev.floffah.gamermode.server.cache.CacheProvider;
import dev.floffah.gamermode.server.packet.PacketTranslator;
import dev.floffah.gamermode.server.socket.SocketConnection;
import dev.floffah.gamermode.server.socket.SocketManager;
import dev.floffah.gamermode.util.concurrent.DaemonThreadFactory;
import dev.floffah.gamermode.util.concurrent.ServerThreadFactory;
import dev.floffah.gamermode.visual.gui.GuiWindow;
import dev.floffah.gamermode.world.WorldManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.logging.log4j.LogManager;

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
    protected org.apache.logging.log4j.Logger logger;

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
    protected File rootDir;

    /**
     * The directory the server stores its data in
     * -- GETTER --
     * Get the directory the server stores its data in
     *
     * @return The directory the server stores its data in
     */
    @Getter
    protected File dataDir;

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
    protected ThreadPoolExecutor pool;

    /**
     * The server's daemon thread pool. This uses a thread factory that automatically marks all threads as daemon threads not user threads.
     * -- GETTER --
     * Get the server's daemon thread pool
     *
     * @return The server's daemon thread pool
     */
    @Getter
    protected ThreadPoolExecutor daemonPool;

    /**
     * The server's scheduled thread pool
     * -- GETTER --
     * Get the server's scheduled thread pool
     *
     * @return The server's scheduled thread pool
     */
    @Getter
    protected ScheduledThreadPoolExecutor scheduler;

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
    protected ThreadPoolExecutor taskPool;

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
     * The server's world manager
     * -- GETTER --
     * Get the server's world manager
     *
     * @return The server's world manager
     */
    @Getter
    protected WorldManager worldManager;

    /**
     * The server's protocol version
     * -- GETTER --
     * Get the server's protocol version
     *
     * @return The server's protocol version
     */
    @Getter
    protected int protocolVersion = 757;

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

    /**
     * All online players
     * -- GETTER --
     * Get all online players
     *
     * @return All online players
     */
    @Getter
    protected Map<UUID, Player> players = new HashMap<>();

    /**
     * The server's console
     * -- GETTER --
     * Get the server's console
     *
     * @return The server's console
     */
    @Getter
    protected Console console;

    ObjectMapper om;
    File configFile;
    private int latestEntityID = Integer.MIN_VALUE;

    /**
     * Whether the server is stopping/has stopped or not
     * -- GETTER --
     * Get whether the server is stopping/has stopped or not
     *
     * @return Whether the server is stopping/has stopped or not
     */
    @Getter
    private boolean stopping = false;

    /**
     * Server's unique id
     */
    @Getter
    private UUID uniqueId;

    /**
     * The server's command executor and manager
     * -- GETTER --
     * Get the server's command executor and manager
     *
     * @return The server's command executor and manager
     */
    @Getter
    private CommandManager commands;

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
        this.logger = LogManager.getLogger(GamerMode.class);

        // events
        this.events = new EventEmitter(this);

        this.commands = new CommandManager(this);

        // visual
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

        // config
        this.om = new ObjectMapper(new YAMLFactory());
        try {
            // get root dir
            if (this.args.contains("-usewd")) {
                this.rootDir =
                    Paths.get(System.getProperty("user.dir")).toFile();
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
                        .toFile();
            }
            // create or load config
            this.configFile =
                Path.of(this.rootDir.getPath(), "config.yml").toFile();
            if (!configFile.exists()) {
                this.config = new Config();
                saveConfig();
            } else {
                loadConfig();
            }
        } catch (URISyntaxException | IOException e) {
            this.fatalShutdown(e);
        }
        this.uniqueId = this.config.info.uuid;

        // directories
        this.dataDir = Path.of(this.rootDir.getPath(), "data").toFile();
        dataDir.mkdirs();

        // threading
        this.pool =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(
                this.config.performance.poolSize
            );
        this.pool.setThreadFactory(new ServerThreadFactory("pooled"));
        this.daemonPool =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(
                this.config.performance.poolSize
            );
        this.daemonPool.setThreadFactory(new DaemonThreadFactory());
        this.scheduler =
            (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
                this.config.performance.scheduledPoolSize
            );
        this.scheduler.setThreadFactory(new ServerThreadFactory("scheduled"));
        this.taskPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.taskPool.setThreadFactory(new ServerThreadFactory("cached"));

        // cache
        this.cache = new CacheProvider(this);

        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            this.keyPairGenerator.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            this.fatalShutdown(e);
        }

        this.worldManager = new WorldManager(this);
        this.worldManager.loadWorlds();

        try {
            this.console = new Console(this);
            this.console.getRenderer().startOutput();
        } catch (IOException e) {
            this.logger.fatal("Failed to initialise the console");
            this.fatalShutdown(e);
            return;
        }

        this.getLogger().info("Server initialised. Starting socket...");

        PacketTranslator.storePackets();

        this.sock = new SocketManager(this);
        try {
            this.sock.start();
        } catch (IOException e) {
            this.fatalShutdown(e);
        }

        this.commands.initialise();

        this.events.execute(new ServerLoadEvent());
    }

    public boolean log4jConfigSet() {
        try {
            System.getProperty("log4j2.configurationFile");
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public int getNextEntityID() {
        if (this.latestEntityID == Integer.MAX_VALUE) return (
            latestEntityID = Integer.MIN_VALUE
        );
        return latestEntityID++;
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
        this.getLogger()
            .fatal("Fatal error occurred that requires shutdown", e);
        try {
            this.shutdown(1);
        } catch (IOException e1) {
            this.getLogger()
                .fatal(
                    "Fatal error while shutting down due to previous fatal error",
                    e1
                );
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
        this.stopping = true;

        TextComponent shutDownMessage = LegacyComponentSerializer
            .legacyAmpersand()
            .deserialize(this.getConfig().messages.shutDownMessage);
        for (SocketConnection connection : this.sock.getConnections()) {
            connection.disconnect(shutDownMessage);
            this.sock.disposeConnection(connection);
        }

        try {
            this.loadConfig();
        } catch (Exception ignored) {}
        this.saveConfig();

        if (this.gui != null) this.gui.stop();
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
