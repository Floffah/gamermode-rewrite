package dev.floffah.gamermode.world;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.util.UUIDUtil;
import java.io.*;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;

public class WorldStorage {

    /**
     * The world being generated
     * -- GETTER --
     * Get the world being generated
     *
     * @return The world being generated
     */
    @Getter
    protected World world;

    /**
     * The file storing the world's UUID
     * -- GETTER --
     * Get the file storing the world's UUID
     *
     * @return The file storing the world's UUID
     */
    @Getter
    protected File uidFile;

    /**
     * The file storing information about the level
     * -- GETTER --
     * Get the file storing information about the level
     *
     * @return The file storing information about the level
     */
    @Getter
    protected File levelFile;

    public WorldStorage(World world) {
        this.world = world;
    }

    public void initialiseStorage() throws IOException {
        // uid.dat
        this.uidFile =
            Path.of(this.world.getDir().getPath(), "uid.dat").toFile();
        if (this.uidFile.exists()) this.readUID(); else {
            this.world.uniqueId = UUID.randomUUID();
            this.writeUID();
        }
        this.getWorld()
            .getWorldManager()
            .getServer()
            .getLogger()
            .debug("UUID of " + this.world.name + " is " + this.world.uniqueId);

        // level.dat
        this.levelFile =
            Path.of(this.world.getDir().getPath(), "level.dat").toFile();
        if (this.levelFile.exists()) {
            this.readLevel();
            if (this.world.seed == null) {
                this.world.seed = new Random().nextLong();
            }
        }

        this.saveAll();
    }

    public void saveAll() throws IOException {
        this.getWorld()
            .getWorldManager()
            .getServer()
            .getLogger()
            .info("Saving " + this.world.name);

        this.writeUID();
        this.writeLevel();
    }

    public UUID readUID() throws IOException {
        InputStream inputStream = new FileInputStream(this.uidFile);
        ByteArrayDataInput in = ByteStreams.newDataInput(
            inputStream.readAllBytes()
        );

        int[] uuidParts = new int[] {
            in.readInt(),
            in.readInt(),
            in.readInt(),
            in.readInt(),
        };
        return this.world.uniqueId = UUIDUtil.intArrayToUUID(uuidParts);
    }

    public void writeUID() throws IOException {
        this.world.uniqueId = UUID.randomUUID();
        int[] uuidParts = UUIDUtil.uuidToIntArray(this.world.uniqueId);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (int i : uuidParts) out.writeInt(i);

        OutputStream outputStream = new FileOutputStream(this.uidFile);
        outputStream.write(out.toByteArray());
        outputStream.flush();
        outputStream.close();
    }

    public void readLevel() throws IOException {
        NamedTag levelNBT = NBTUtil.read(this.levelFile);
        Tag<?> levelNBTTag = levelNBT.getTag();
        if (!(levelNBTTag instanceof CompoundTag)) throw new IOException(
            "Invalid level.dat"
        );

        CompoundTag levelTag = (CompoundTag) levelNBTTag;
        CompoundTag dataTag = levelTag.getCompoundTag("Data");

        if (dataTag.containsKey("LevelName")) this.world.levelName =
            dataTag.getString("LevelName");
        if (dataTag.containsKey("ServerBrands")) {
            ListTag<StringTag> serverBrands = dataTag
                .getListTag("ServerBrands")
                .asStringTagList();
            this.world.miscData.serverBrands.clear();
            serverBrands.forEach(brand -> {
                this.world.miscData.serverBrands.add(brand.getValue());
            });
            if (
                !this.world.miscData.serverBrands.contains("GamerMode")
            ) this.world.miscData.serverBrands.add("GamerMode");
        }

        if (dataTag.containsKey("WorldGenSettings")) {
            CompoundTag worldGenTag = dataTag.getCompoundTag(
                "WorldGenSettings"
            );

            if (worldGenTag.containsKey("seed")) this.world.seed =
                worldGenTag.getLong("seed");
        }
    }

    public void writeLevel() throws IOException {
        CompoundTag levelTag = new CompoundTag();
        CompoundTag dataTag = new CompoundTag();

        dataTag.putString("LevelName", this.world.levelName);

        ListTag<StringTag> serverBrands = new ListTag<>(StringTag.class);
        this.world.miscData.serverBrands.forEach(brand ->
                serverBrands.add(new StringTag(brand))
            );
        dataTag.put("ServerBrands", serverBrands);

        CompoundTag worldGenTag = new CompoundTag();

        worldGenTag.putLong("seed", this.world.seed);

        dataTag.put("WorldGenSettings", worldGenTag);

        levelTag.put("Data", dataTag);

        //new NamedTag(null, levelTag)
        NBTUtil.write(levelTag, this.levelFile);
    }
}
