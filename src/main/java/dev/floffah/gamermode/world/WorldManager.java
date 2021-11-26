package dev.floffah.gamermode.world;

import dev.floffah.gamermode.player.Player;
import dev.floffah.gamermode.server.Server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
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

    // directories
    private File worldDir;
    private File worldPlayerDataDir;

    private File worldNetherDir;
    private File worldEndDir;

    public WorldManager(Server server) {
        this.server = server;
    }

    public void loadWorlds() {
        this.server.getLogger().info("Loading world");

        // world
        this.worldDir =
            Path.of(this.server.getDataDir().getPath(), "world").toFile();
        this.worldPlayerDataDir =
            Path.of(this.worldDir.getPath(), "playerdata").toFile();

        if (!this.worldDir.exists()) this.worldDir.mkdirs();
        if (!this.worldPlayerDataDir.exists()) this.worldPlayerDataDir.mkdirs();

        // world_nether
        this.worldNetherDir =
            Path
                .of(this.server.getDataDir().getPath(), "world_nether")
                .toFile();

        if (!this.worldNetherDir.exists()) this.worldNetherDir.mkdirs();

        // world_the_end
        this.worldEndDir =
            Path
                .of(this.server.getDataDir().getPath(), "world_the_end")
                .toFile();

        if (!this.worldEndDir.exists()) this.worldEndDir.mkdirs();
    }

    public boolean hasRawPlayerData(UUID uuid) {
        return Path
            .of(this.worldPlayerDataDir.getPath(), uuid.toString() + ".dat")
            .toFile()
            .exists();
    }

    public CompoundTag readRawPlayerData(UUID uuid) throws IOException {
        File playerData = Path
            .of(this.worldPlayerDataDir.getPath(), uuid.toString() + ".dat")
            .toFile();

        if (!playerData.exists()) return null;

        NamedTag tag = NBTUtil.read(playerData);
        if (!(tag.getTag() instanceof CompoundTag)) return null;

        return (CompoundTag) tag.getTag();
    }

    public void writeRawPlayerData(Player player) throws IOException {
        File playerData = Path
            .of(
                this.worldPlayerDataDir.getPath(),
                player.getUniqueId().toString() + ".dat"
            )
            .toFile();

        CompoundTag tag = new CompoundTag();

        long uuidMost = player.getUniqueId().getMostSignificantBits();
        int uuidMostA = (int) (uuidMost >> 32);
        int uuidMostB = (int) uuidMost;
        long uuidLeast = player.getUniqueId().getLeastSignificantBits();
        int uuidLeastA = (int) (uuidLeast >> 32);
        int uuidLeastB = (int) uuidLeast;

        tag.putIntArray(
            "UUID",
            new int[] { uuidMostA, uuidMostB, uuidLeastA, uuidLeastB }
        );

        NBTUtil.write(tag, playerData, true);
    }
}
