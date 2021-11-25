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
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;

public class Server {

    public static Server server;

    // output
    public Logger logger;
    public GuiWindow gui;

    // context
    public List<String> args;
    public boolean debugMode;

    // config
    public ObjectMapper om;
    public File configFile;
    public Config config;

    // directories
    public String rootDir;
    public Path dataDir;

    // util
    public EventEmitter events;
    public CacheProvider cache;

    // threading
    public ExecutorService pool;
    public ScheduledExecutorService scheduler;
    /**
     * Only use the task pool for quick and short tasks. For anything else, use the thread pool or the scheduled pool.
     */
    public ExecutorService taskPool;

    // encryption
    public KeyPairGenerator keyPairGenerator;

    // nbt
    public NBTSerializer nbtSerializer;
    public NBTSerializer compressedNbtSerializer;
    public NBTDeserializer nbtDeserializer;
    public NBTDeserializer compressedNbtDeserializer;

    // io
    public SocketManager sock;

    // meta
    public int protocolVersion = 756;

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
        this.logger.info(
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

        // nbt
        this.nbtSerializer = new NBTSerializer();
        this.compressedNbtSerializer = new NBTSerializer(true);
        this.nbtDeserializer = new NBTDeserializer();
        this.compressedNbtDeserializer = new NBTDeserializer(true);

        this.logger.info("Server initialised. Starting socket...");

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
        this.logger.printStackTrace(e);
        try {
            this.shutdown(1);
        } catch (IOException e1) {
            this.logger.printStackTrace(e1);
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
        this.logger.info("Goodbye!");

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
