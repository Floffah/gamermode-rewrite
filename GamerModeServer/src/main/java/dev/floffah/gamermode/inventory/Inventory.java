package dev.floffah.gamermode.inventory;

import java.util.List;

public interface Inventory {
    /**
     * Get the total amount of slots this inventory has.
     *
     * @return The total amount of slots this inventory has.
     */
    default int getTotalSlots() {
        int lastBlockedIndex = 0;

        for (InventorySection section : this.getSections()) {
            if (section.to() > lastBlockedIndex) lastBlockedIndex = section.to();
        }

        return lastBlockedIndex;
    }

    /**
     * The sections this inventory has.
     *
     * @return The sections this inventory has.
     */
    List<InventorySection> getSections();

    /**
     * The name of the inventory.
     *
     * @return The name of the inventory.
     */
    String getName();
}
