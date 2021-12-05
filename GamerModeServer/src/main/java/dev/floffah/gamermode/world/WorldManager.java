package dev.floffah.gamermode.world;

import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.entity.player.Player;
import dev.floffah.gamermode.server.Server;
import dev.floffah.gamermode.world.biome.Biome;
import dev.floffah.gamermode.world.dimension.DimensionType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

public class WorldManager {

    /**
     * All worlds the server is aware of
     * -- GETTER --
     * Get all worlds the server is aware of
     *
     * @return All worlds the server is aware of
     */
    @Getter
    private final Map<UUID, World> worlds = new HashMap<>();

    // worlds
    /**
     * All biomes known to the server
     * -- GETTER --
     * Get all biomes known to the server
     *
     * @return All biomes known to the server
     */
    @Getter
    private final Map<Identifier, Biome> biomes = new HashMap<>();

    /**
     * The server instance
     * -- GETTER --
     * Get the server instance
     *
     * @return The server instance
     */
    @Getter
    protected Server server;

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
     * Cached dimension codec
     * -- GETTER --
     * Get the cached dimension codec
     *
     * @return The cached dimension codec
     */
    @Getter
    private CompoundTag dimensionCodec;

    public WorldManager(Server server) {
        this.server = server;
    }

    public void loadWorlds() {
        this.server.getLogger().info("Loading world");

        String worldname = this.server.getConfig().worlds.worldname;

        // biomes

        this.addBiome(Biome.PLAINS);

        // worlds

        World world = new World(this, WorldType.OVERWORLD, worldname);
        world.dimTypes.add(DimensionType.DEFAULT_OVERWORLD);
        world.dimTypes.add(DimensionType.DEFAULT_OVERWORLD_CAVES);
        this.overworld = world;
        this.worlds.put(world.getUniqueId(), world);

        this.nether =
            new World(this, WorldType.NETHER, worldname + "_nether", world);
        this.nether.dimTypes.add(DimensionType.DEFAULT_THE_NETHER);
        this.worlds.put(this.nether.getUniqueId(), this.nether);

        this.end =
            new World(this, WorldType.END, worldname + "_the_end", world);
        this.end.dimTypes.add(DimensionType.DEFAULT_THE_END);
        this.worlds.put(this.end.getUniqueId(), this.end);
    }

    public void addBiome(Biome biome) {
        this.biomes.put(biome.getName(), biome);
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
        if (this.dimensionCodec == null) {
            CompoundTag data = new CompoundTag();

            CompoundTag dimType = new CompoundTag();

            dimType.putString("type", "minecraft:dimension_type");

            ListTag<CompoundTag> dimRegistry = new ListTag<>(CompoundTag.class);

            int dimId = 0;
            for (World world : this.worlds.values()) {
                CompoundTag dimTypeElements = world.buildDimType();

                CompoundTag dimTypeRegistry = new CompoundTag();
                dimTypeRegistry.putString("name", world.getType().getName());
                dimTypeRegistry.putInt("id", dimId);
                dimTypeRegistry.put("element", dimTypeElements);

                dimRegistry.add(dimTypeRegistry);

                dimId++;

                CompoundTag dimCaves = world.buildCavesDimType();
                if (dimCaves != null) {
                    CompoundTag dimCavesRegistry = new CompoundTag();
                    dimCavesRegistry.putString(
                        "name",
                        world.getType().getName() + "_caves"
                    );
                    dimCavesRegistry.putInt("id", dimId);
                    dimCavesRegistry.put("element", dimCaves);

                    dimRegistry.add(dimCavesRegistry);

                    dimId++;
                }
            }

            dimType.put("value", dimRegistry);

            data.put("minecraft:dimension_type", dimType);

            CompoundTag worldGenBiome = new CompoundTag();
            worldGenBiome.putString("type", "minecraft:worldgen/biome");

            ListTag<CompoundTag> biomeRegistry = new ListTag<>(
                CompoundTag.class
            );

            int biomeId = 0;
            for (Biome biome : this.biomes.values()) {
                CompoundTag biomeRegistryEntry = new CompoundTag();
                biomeRegistryEntry.putString(
                    "name",
                    biome.getName().toString()
                );
                biomeRegistryEntry.putInt("id", biomeId);
                biomeRegistryEntry.put(
                    "element",
                    Biome.buildBiomeElement(biome)
                );

                biomeRegistry.add(biomeRegistryEntry);

                biomeId++;
            }

            worldGenBiome.put("value", biomeRegistry);

            data.put("minecraft:worldgen/biome", worldGenBiome);
            this.dimensionCodec = data;
        }

        return this.dimensionCodec;
    }
}
