package dev.floffah.gamermode.entity.player;

import dev.floffah.gamermode.inventory.EntityInventory;
import dev.floffah.gamermode.inventory.InventorySection;
import dev.floffah.gamermode.inventory.InventorySectionType;
import lombok.Getter;

import java.util.List;

public class PlayerInventory implements EntityInventory {

    @Getter
    private final Player entity;

    // see https://wiki.vg/images/1/13/Inventory-slots.png
    @Getter
    private final List<InventorySection> sections = List.of(
        new InventorySection(0, 1, InventorySectionType.CRAFTING_OUTPUT),
        new InventorySection(1, 5, InventorySectionType.CRAFTING_INPUT),
        new InventorySection(5, 9, InventorySectionType.ARMOUR),
        new InventorySection(9, 36, InventorySectionType.MAIN_INVENTORY),
        new InventorySection(36, 45, InventorySectionType.HOTBAR),
        new InventorySection(45, 46, InventorySectionType.OFFHAND)
    );

    /**
     * The player's currently selected slot
     * -- GETTER --
     * Gets the currently selected slot
     *
     * @return The currently selected slot
     */
    @Getter
    private byte selectedSlot = 0;

    public PlayerInventory(Player entity) {
        this.entity = entity;
    }

    @Override
    public String getName() {
        return "Inventory";
    }
}
