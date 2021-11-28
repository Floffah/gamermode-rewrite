package dev.floffah.gamermode.player;

import dev.floffah.gamermode.entity.Entity;
import dev.floffah.gamermode.server.socket.SocketConnection;
import dev.floffah.gamermode.world.World;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class Player implements Entity {

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

    /**
     * The player's entity ID
     * -- GETTER --
     * Get the player's entity ID
     *
     * @return The player's entity ID
     */
    @Getter
    protected int entityID;

    /**
     * The player's current gamemode
     * -- GETTER --
     * Get the player's current gamemode
     *
     * @return The player's current gamemode
     * -- SETTER --
     * Set the player's current gamemode
     *
     * @param mode The player's current gamemode
     */
    @Getter
    @Setter
    protected int gameMode;

    /**
     * the players previous game mode
     * -- GETTER --
     * Get the player's previous gamemode
     *
     * @return The player's previous gamemode
     */
    @Getter
    protected int previousGameMode;

    /**
     * The player's current world
     * -- GETTER --
     * Get the player's current world
     *
     * @return The player's current world
     */
    @Getter
    protected World world;

    public Player(SocketConnection conn) {
        this.conn = conn;

        this.profile = new Profile(this);

        this.entityID = conn.getSocketManager().getServer().getNextEntityID();
        this.previousGameMode =
            this.conn.getSocketManager()
                .getServer()
                .getConfig()
                .worlds.defaultGamemode;
        this.world =
            this.getConn()
                .getSocketManager()
                .getServer()
                .getWorldManager()
                .getOverworld(); // this is overwritten and is just for null issues
    }
}
