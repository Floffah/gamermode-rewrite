package dev.floffah.gamermode.inventory;

/**
 * @param from First index of the section, e.g: <strong>20</strong> 21 22 23 24 25 26 27 28 for section 20-28
 * @param to Index to stop at in the section, e.g: 20 21 22 23 24 25 26 27 28 <strong>29</strong> for section 20-28.
 * A section does not include the to index, it is the index to stop at. So if you set `to` to 29, 29 will not be part of the section but 28 will.
 * @param type The type of section
 */
public record InventorySection(int from, int to, InventorySectionType type) {
}