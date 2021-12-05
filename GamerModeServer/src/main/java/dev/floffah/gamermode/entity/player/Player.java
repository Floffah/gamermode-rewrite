package dev.floffah.gamermode.entity.player;

import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.entity.DamageableEntity;
import dev.floffah.gamermode.entity.Entity;
import dev.floffah.gamermode.server.Server;
import dev.floffah.gamermode.server.packet.play.message.PluginMessage;
import dev.floffah.gamermode.server.socket.SocketConnection;
import dev.floffah.gamermode.world.World;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class Player implements Entity, DamageableEntity, Identified {

    /**
     * The player's connection
     * -- GETTER --
     * Get the player's connection
     *
     * @return The player's connection
     */
    @Getter
    private final SocketConnection conn;

    /**
     * The player's profile
     * -- GETTER --
     * Get the player's profile
     *
     * @return The player's profile
     */
    @Getter
    private final Profile profile;

    /**
     * The player's entity ID
     * -- GETTER --
     * Get the player's entity ID
     *
     * @return The player's entity ID
     */
    @Getter
    private final int entityId;

    /**
     * The player's abilities
     * -- GETTER --
     * Get the player's abilities
     *
     * @return The player's abilities
     */
    @Getter
    private final Abilities abilities;

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
     * The player's current gamemode
     * -- GETTER --
     * Get the player's current gamemode
     *
     * @return The player's current gamemode
     * -- SETTER --
     * Set the player's current gamemode
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
     * The player's UUID
     * -- GETTER --
     * Get the player's UUID
     *
     * @return The player's UUID
     */
    @Getter
    private UUID uniqueId;

    /**
     * The player's current world
     */
    @Getter
    private World world;

    /**
     * Claimed brand by the player
     * -- GETTER --
     * Get the player's claimed brand
     *
     * @return The player's claimed brand
     * -- SETTER --
     * Set the player's claimed brand
     * @param brand The player's claimed brand
     */
    @Getter
    @Setter
    private String brand;

    /**
     * The player's movement state
     * -- GETTER --
     * Get the player's movement state
     *
     * @return The player's movement state
     */
    @Getter
    private PlayerMovement movement;

    /**
     * Whether the player has finished the login process
     * -- GETTER --
     * Get whether the player has finished the login process
     *
     * @return Whether the player has finished the login process
     */
    @Getter
    private boolean joined = false;

    /**
     * The player's current health
     */
    @Getter
    private float health;

    private PlayerIdentity identity;

    public Player(SocketConnection conn) {
        this.conn = conn;

        this.profile = new Profile(this);
        this.abilities = new Abilities(this);
        this.identity = new PlayerIdentity(this);
        this.movement = new PlayerMovement(this);

        this.entityId = conn.getSocketManager().getServer().getNextEntityID();
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

    public void sendPluginMessage(
        Identifier channel,
        ByteArrayDataOutput message
    ) throws IOException {
        this.conn.send(new PluginMessage(channel, message));
    }

    @Override
    public void applySavableData(CompoundTag tag, Server server) {
        DamageableEntity.super.applySavableData(tag, server);

        // gamemode
        tag.putByte(
            "previousPlayerGameType",
            (byte) this.getPreviousGameMode()
        );
        tag.putByte("playerGameType", (byte) this.getGameMode());
    }

    @Override
    public void readSavableData(CompoundTag tag, Server server) {
        DamageableEntity.super.readSavableData(tag, server);

        // gamemode
        if (tag.containsKey("previousPlayerGameType")) {
            this.previousGameMode = tag.getByte("previousPlayerGameType");
        }

        if (
            this.getConn()
                .getSocketManager()
                .getServer()
                .getConfig()
                .worlds.enforceDefaultGamemode ||
            !tag.containsKey("playerGameType")
        ) {
            this.gameMode =
                this.getConn()
                    .getSocketManager()
                    .getServer()
                    .getConfig()
                    .worlds.defaultGamemode;
        } else {
            this.gameMode = tag.getByte("playerGameType");
        }
    }

    @Override
    public void setUniqueId(UUID uniqueId) {
        if (this.uniqueId != null) throw new IllegalStateException(
            "Cannot change UUID once set"
        );
        this.uniqueId = uniqueId;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
        if (this.isJoined()) {
            // TODO: send world set packets
        }
    }

    public void setHasJoined(boolean hasJoined) {
        if (this.joined) throw new IllegalStateException(
            "Cannot change joined state once set"
        );
        this.joined = hasJoined;
    }

    public void setHealth(float health) {
        this.health = health;
        if (this.joined) {
            // TODO: send health packets
        }
    }

    @Override
    public @NotNull Identity identity() {
        return this.identity;
    }
}
