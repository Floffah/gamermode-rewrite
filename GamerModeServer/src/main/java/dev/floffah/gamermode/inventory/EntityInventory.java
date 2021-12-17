package dev.floffah.gamermode.inventory;

import dev.floffah.gamermode.entity.Entity;

public interface EntityInventory extends Inventory {
    /**
     * Get the entity whose inventory this is
     * @return The entity
     */
    Entity getEntity();

    @Override
    default String getName() {
        return this.getEntity().getName();
    }
}
