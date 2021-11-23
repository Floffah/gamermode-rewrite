package dev.floffah.gamermode.config;

public class Config {
    /**
     * Player related settings
     */
    public PlayerConf players = new PlayerConf();
    /**
     * Main server info settings
     */
    public ServerInfo info = new ServerInfo();
    /**
     * World related settings
     */
    public WorldInfo worlds = new WorldInfo();
    /**
     * Performance settings
     */
    public Performance performance = new Performance();

    /**
     * Player related settings
     */
    public static class PlayerConf {
        /**
         * Maximum players
         */
        public int max = 20;
    }

    /**
     * Main server info settings
     */
    public static class ServerInfo {
        /**
         * Server's server list MOTD. Supports color codes using an ampersand (\u0026)
         */
        public String motd = "Minecraft server";
        /**
         * The server's difficulty<br/>
         * 0 - peaceful<br/>
         * 1 - easy<br/>
         * 2 - normal<br/>
         * 3 - hard<br/>
         */
        public byte difficulty = 1;
        /**
         * Server port to listen on
         */
        public int port = 25565;
    }

    /**
     * World settings
     */
    public static class WorldInfo {
        /**
         * Name of the world. Affects storage names.
         */
        public String worldname = "world";
        /**
         * Forced render distance. Between 8 and 12 is good for the best performance while still being playable
         */
        public int renderDistance = 8;
    }

    /**
     * Performance settings
     */
    public static class Performance {
        /**
         * Size of the thread pool
         */
        public int poolSize = 10;
        /**
         * Size of the scheduler thread pool
         */
        public int scheduledPoolSize = 10;
    }
}
