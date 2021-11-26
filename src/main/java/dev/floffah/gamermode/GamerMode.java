package dev.floffah.gamermode;

import dev.floffah.gamermode.server.Server;

public class GamerMode {

    /**
     * Gamermode entrypoint
     * @param args Process arguments
     */
    public static void main(String[] args) {
        Server server = new Server(args);
    }
}
