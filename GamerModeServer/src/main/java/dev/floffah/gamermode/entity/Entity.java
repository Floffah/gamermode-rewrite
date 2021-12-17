package dev.floffah.gamermode.entity;

import dev.floffah.gamermode.datatype.util.UUIDUtil;
import dev.floffah.gamermode.server.Server;
import dev.floffah.gamermode.world.World;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntArrayTag;

public interface Entity {
    /**
     * Get the entity's entity ID
     *
     * @return The entity's entity ID
     */
    int getEntityId();

    /**
     * Get the entity's UUID
     *
     * @return The entity's UUID
     */
    UUID getUniqueId();

    /**
     * Set the entity's UUID.
     * This **will** fail if the entity already has a UUID.
     *
     * @param uuid The entity's UUID
     */
    void setUniqueId(UUID uuid);

    /**
     * Get the entity's movement state
     *
     * @return The entity's movement state
     */
    EntityMovement getMovement();

    /**
     * Get the entity's current world
     *
     * @return The entity's current world
     */
    World getWorld();

    /**
     * Set the entity's current world
     *
     * @param world The world to send the entity to
     */
    void setWorld(World world);

    /**
     * Get the entity's name
     *
     * @return The entity's name
     */
    String getName();

    /**
     * Apply NBT data to the entity. This is called when saving.
     * When overriding, make sure to call Entity.super.applySavableData(tag) for every entity-based interface being implemented (including entity) first
     *
     * @param tag The root compound tag. This may be empty.
     */
    default void applySavableData(CompoundTag tag, Server server) {
        // uuid
        tag.putIntArray("UUID", UUIDUtil.uuidToIntArray(this.getUniqueId()));

        // world
        tag.putLong(
            "WorldUUIDMost",
            this.getUniqueId().getMostSignificantBits()
        );
        tag.putLong(
            "WorldUUIDLeast",
            this.getUniqueId().getLeastSignificantBits()
        );
    }

    /**
     * Read NBT data from the entity. This is called when loading.
     * When overriding, make sure to call Entity.super.readSavableData(tag) for every entity-based interface being implemented (including entity) first
     *
     * @param tag The root compound tag. If this is a playerdata file, this will be the whole files read data, if it is an entity, it will be an entry in an entity MCA file
     */
    default void readSavableData(CompoundTag tag, Server server) {
        // uuid
        int[] uuidInts = ((IntArrayTag) tag.get("UUID")).getValue();
        UUID uuid = UUIDUtil.intArrayToUUID(uuidInts);

        if (
            this.getUniqueId() != null && !this.getUniqueId().equals(uuid)
        ) this.setUniqueId(uuid);

        // world
        long worldUUIDMost = tag.getLong("WorldUUIDMost");
        long worldUUIDLeast = tag.getLong("WorldUUIDLeast");
        UUID worldUUID = new UUID(worldUUIDMost, worldUUIDLeast);

        if (server.getWorldManager().getWorlds().containsKey(worldUUID)) {
            this.setWorld(server.getWorldManager().getWorlds().get(worldUUID));
        }
    }
}
