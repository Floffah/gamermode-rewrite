package dev.floffah.gamermode.player;

import dev.floffah.gamermode.server.socket.SocketConnection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class Player {

    /**
     * The player's connection
     * -- GETTER --
     * Get the player's connection
     *
     * @return The player's connection
     */
    @Getter
    protected SocketConnection conn;

    /**
     * The player's profile
     * -- GETTER --
     * Get the player's profile
     *
     * @return The player's profile
     */
    @Getter
    protected Profile profile;

    /**
     * The player's username
     * -- GETTER --
     * Get the player's username
     *
     * @return The player's username
     */
    @Getter
    protected String username = null;

    /**
     * The player's UUID
     * -- GETTER --
     * Get the player's UUID
     *
     * @return The player's UUID
     */
    @Getter
    protected UUID uniqueId;

    public Player(SocketConnection conn) {
        this.conn = conn;

        this.profile = new Profile(this);
    }
}
