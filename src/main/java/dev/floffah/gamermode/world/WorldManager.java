package dev.floffah.gamermode.world;

import dev.floffah.gamermode.player.Player;
import dev.floffah.gamermode.server.Server;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;

public class WorldManager {

    /**
     * The server instance
     * -- GETTER --
     * Get the server instance
     *
     * @return The server instance
     */
    @Getter
    protected Server server;

    // worlds
    /**
     * The overworld
     * -- GETTER --
     * Get the overworld
     *
     * @return The overworld
     */
    @Getter
    protected World overworld;

    /**
     * The nether
     * -- GETTER --
     * Get the nether
     *
     * @return The nether
     */
    @Getter
    protected World nether;

    /**
     * The end
     * -- GETTER --
     * Get the end
     *
     * @return The end
     */
    @Getter
    protected World end;

    /**
     * All worlds the server is aware of
     * -- GETTER --
     * Get all worlds the server is aware of
     *
     * @return All worlds the server is aware of
     */
    @Getter
    protected List<World> worlds;

    public WorldManager(Server server) {
        this.server = server;
    }

    public void loadWorlds() {
        this.server.getLogger().info("Loading world");

        String worldname = this.server.getConfig().worlds.worldname;

        World world = new World(this, WorldType.OVERWORLD, worldname);
        this.overworld = world;

        this.nether = new World(
            this,
            WorldType.NETHER,
            worldname + "_nether",
            world
        );

        this.end = new World(
            this,
            WorldType.END,
            worldname + "_the_end",
            world
        );
    }

    public boolean hasRawPlayerData(UUID uuid) {
        // once more worlds are supported this may need to be made to check what world a player is in
        return this.overworld.hasRawPlayerData(uuid);
    }

    public CompoundTag readRawPlayerData(UUID uuid) throws IOException {
        return this.overworld.readRawPlayerData(uuid);
    }

    public void writeRawPlayerData(Player player) throws IOException {
        this.overworld.writeRawPlayerData(player);
    }
}
