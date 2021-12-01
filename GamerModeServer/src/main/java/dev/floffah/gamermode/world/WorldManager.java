package dev.floffah.gamermode.world;

import dev.floffah.gamermode.player.Player;
import dev.floffah.gamermode.server.Server;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

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
    protected Map<UUID, World> worlds = new HashMap<>();

    public WorldManager(Server server) {
        this.server = server;
    }

    public void loadWorlds() {
        this.server.getLogger().info("Loading world");

        String worldname = this.server.getConfig().worlds.worldname;

        World world = new World(this, WorldType.OVERWORLD, worldname);
        this.overworld = world;
        this.worlds.put(world.getUniqueId(), world);

        this.nether =
            new World(this, WorldType.NETHER, worldname + "_nether", world);
        this.worlds.put(this.nether.getUniqueId(), this.nether);

        this.end =
            new World(this, WorldType.END, worldname + "_the_end", world);
        this.worlds.put(this.end.getUniqueId(), this.end);
    }

    public boolean hasRawPlayerData(UUID uuid) {
        if (this.server.getPlayers().containsKey(uuid)) {
            Player player = this.server.getPlayers().get(uuid);
            if (player.getWorld() != null) return player
                .getWorld()
                .hasRawPlayerData(uuid);
        }

        return this.overworld.hasRawPlayerData(uuid);
    }

    public CompoundTag readRawPlayerData(UUID uuid) throws IOException {
        if (this.server.getPlayers().containsKey(uuid)) {
            Player player = this.server.getPlayers().get(uuid);
            if (player.getWorld() != null) return player
                .getWorld()
                .readRawPlayerData(uuid);
        }

        return this.overworld.readRawPlayerData(uuid);
    }

    public void writeRawPlayerData(Player player) throws IOException {
        if (player.getWorld() != null) {
            player.getWorld().writeRawPlayerData(player);
            return;
        }

        this.overworld.writeRawPlayerData(player);
    }

    public CompoundTag buildDimensionCodec() {
        CompoundTag data = new CompoundTag();

        CompoundTag dimType = new CompoundTag();

        dimType.putString("type", "minecraft:dimension_type");

        ListTag<CompoundTag> dimRegistry = new ListTag<>(CompoundTag.class);

        int id = 0;
        for (World world : this.worlds.values()) {
            CompoundTag dimTypeElements = world.buildDimType();

            CompoundTag dimTypeRegistry = new CompoundTag();
            dimTypeRegistry.putString("name", world.getType().getName());
            dimTypeRegistry.putInt("id", id);
            dimTypeRegistry.put("element", dimTypeElements);

            dimRegistry.add(dimTypeRegistry);

            id++;

            CompoundTag dimCaves = world.buildCavesDimType();
            if (dimCaves != null) {
                CompoundTag dimCavesRegistry = new CompoundTag();
                dimCavesRegistry.putString(
                    "name",
                    world.getType().getName() + "_caves"
                );
                dimCavesRegistry.putInt("id", id);
                dimCavesRegistry.put("element", dimCaves);

                dimRegistry.add(dimCavesRegistry);

                id++;
            }
        }

        dimType.put("value", dimRegistry);

        data.put("minecraft:dimension_type", dimType);

        CompoundTag worldGenBiome = new CompoundTag();
        worldGenBiome.putString("type", "minecraft:worldgen/biome");
        worldGenBiome.put("value", new ListTag<>(CompoundTag.class));

        data.put("minecraft:worldgen/biome", worldGenBiome);

        return data;
    }
}
