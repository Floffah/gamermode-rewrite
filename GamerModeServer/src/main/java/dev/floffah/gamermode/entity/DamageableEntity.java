package dev.floffah.gamermode.entity;

import dev.floffah.gamermode.server.Server;
import net.querz.nbt.tag.CompoundTag;

public interface DamageableEntity extends Entity {
    /**
     * Get the entity's health
     *
     * @return The entity's health
     */
    public float getHealth();

    /**
     * Set the entity's health
     *
     * @param health The new health
     */
    public void setHealth(float health);

    @Override
    default void applySavableData(CompoundTag tag, Server server) {
        Entity.super.applySavableData(tag, server);

        tag.putFloat("Health", this.getHealth());
    }

    @Override
    default void readSavableData(CompoundTag tag, Server server) {
        Entity.super.readSavableData(tag, server);

        this.setHealth(tag.getFloat("Health"));
    }
}
