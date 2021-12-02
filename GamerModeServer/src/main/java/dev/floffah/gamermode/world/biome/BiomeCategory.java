package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import lombok.Getter;

public enum BiomeCategory {
    OCEAN("ocean"),
    PLAINS("plains"),
    DESERT("desert"),
    FOREST("forest"),
    EXTREME_HILLS("extreme_hills"),
    TAIGA("taiga"),
    SWAMP("swamp"),
    RIVER("river"),
    NETHER("nether"),
    THE_END("the_end"),
    ICY("icy"),
    MUSHROOM("mushroom"),
    BEACH("beach"),
    JUNGLE("jungle"),
    MESA("mesa"),
    SAVANNA("savanna"),
    NONE("none");

    @Getter
    private final String name;

    BiomeCategory(String name) {
        this.name = name;
    }

    public Identifier getIdentifier() {
        return Identifier.from(this.getName());
    }
}
