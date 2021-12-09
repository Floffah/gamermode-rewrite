package dev.floffah.gamermode.world;

import dev.floffah.gamermode.entity.player.Player;
import dev.floffah.gamermode.world.dimension.DimensionType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class World {

    /**
     * The name of the world
     * -- GETTER --
     * Get the name of the world
     *
     * @return The name of the world
     */
    @Getter
    protected String name;

    /**
     * The level/printed name of the world
     * -- GETTER --
     * Get the level/printed name of the world
     *
     * @return The level/printed name of the world
     */
    @Getter
    protected String levelName;

    /**
     * The parent world
     * -- GETTER --
     * Get the parent world
     *
     * @return The parent world
     */
    @Getter
    @Nullable
    protected World parent;

    /**
     * The world's ype
     * -- GETTER --
     * Get the world's type
     *
     * @return The world's type
     */
    @Getter
    protected WorldType type;

    /**
     * The world's directory
     * -- GETTER --
     * Get the world's directory
     *
     * @return The world's directory
     */
    @Getter
    protected File dir;

    /**
     * The world's manager
     * -- GETTER --
     * Get the world's manager
     *
     * @return The world's manager
     */
    @Getter
    protected WorldManager worldManager;

    /**
     * The world's player data directory
     * -- GETTER --
     * Get the world's player data directory
     *
     * @return The world's player data directory
     */
    @Getter
    @Nullable
    protected File playerDataDir;

    /**
     * The world's generation helper
     * -- GETTER --
     * Get the world's generation helper
     *
     * @return The world's generation helper
     */
    @Getter
    protected WorldStorage storage;

    /**
     * The worlds's UUID
     * -- GETTER --
     * Get the world's UUID
     *
     * @return The world's UUID
     */
    @Getter
    protected UUID uniqueId;

    /**
     * The world's random seed
     * -- GETTER --
     * Get the world's random seed
     *
     * @return The world's random seed
     */
    @Getter
    protected Long seed;

    /**
     * Uncategorized data about the world
     * -- GETTER --
     * Get the world's data
     *
     * @return The world's data
     */
    @Getter
    protected MiscWorldData miscData;

    /**
     * The world's dimension types
     * -- GETTER --
     * Get the world's dimension types
     *
     * @return The world's dimension types
     */
    @Getter
    protected List<DimensionType> dimTypes = new ArrayList<>();

    public World(WorldManager manager, WorldType type, String name) {
        this.worldManager = manager;
        this.name = name;
        this.levelName = name;
        this.type = type;
        this.parent = null;

        this.initialise();
    }

    public World(
        WorldManager manager,
        WorldType type,
        String name,
        @NonNull World parent
    ) {
        this.worldManager = manager;
        this.name = name;
        this.levelName = name;
        this.parent = parent;
        this.type = type;

        this.initialise();
    }

    private void initialise() {
        this.storage = new WorldStorage(this);
        this.miscData = new MiscWorldData(this);

        this.dir =
            Path
                .of(
                    this.getWorldManager().getServer().getDataDir().getPath(),
                    this.getName()
                )
                .toFile();

        try {
            this.storage.initialiseStorage();
        } catch (IOException e) {
            this.getWorldManager()
                .getServer()
                .getLogger()
                .error("Failed to initialise world storage");
            this.getWorldManager().getServer().fatalShutdown(e);
            return;
        }

        if (this.parent != null) {
            this.playerDataDir = null;
        } else {
            this.playerDataDir =
                Path.of(this.dir.getPath(), "playerdata").toFile();
            this.playerDataDir.mkdirs();
        }
    }

    public boolean hasRawPlayerData(UUID uuid) {
        World check = this.parent != null ? this.parent : this;
        return Path
            .of(check.getPlayerDataDir().getPath(), uuid.toString() + ".dat")
            .toFile()
            .exists();
    }

    public CompoundTag readRawPlayerData(UUID uuid) throws IOException {
        World check = this.parent != null ? this.parent : this;
        File playerData = Path
            .of(check.getPlayerDataDir().getPath(), uuid.toString() + ".dat")
            .toFile();

        if (!playerData.exists()) return null;

        NamedTag tag = NBTUtil.read(playerData);
        if (!(tag.getTag() instanceof CompoundTag)) return null;

        return (CompoundTag) tag.getTag();
    }

    public void writeRawPlayerData(Player player) throws IOException {
        World check = this.parent != null ? this.parent : this;
        File playerData = Path
            .of(
                check.getPlayerDataDir().getPath(),
                player.getUniqueId().toString() + ".dat"
            )
            .toFile();

        CompoundTag tag = new CompoundTag();

        player.applySavableData(tag, this.getWorldManager().getServer());

        NBTUtil.write(tag, playerData, true);
    }

    /**
     * Creates dimension type NBT for this world
     *
     * @return The dimension type NBT
     */
    public CompoundTag buildDimType() {
        return this.dimTypes.get(0).toNBT();
    }

    /**
     * Creates dimension type NBT for this world's caves. Null if the world doesn't have caves that are separated into another dimension (nether, end).
     *
     * @return The dimension type NBT
     */
    @Nullable
    public CompoundTag buildCavesDimType() {
        if (this.type == WorldType.OVERWORLD && this.dimTypes.size() > 1) {
            return this.dimTypes.get(1).toNBT();
        }

        return null;
    }
}
