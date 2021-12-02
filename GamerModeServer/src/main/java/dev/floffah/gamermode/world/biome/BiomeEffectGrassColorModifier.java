package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import lombok.Getter;

public enum BiomeEffectGrassColorModifier {
    SWAMP("swamp"),
    DARK_FOREST("dark_forest");

    @Getter
    private final String name;

    BiomeEffectGrassColorModifier(String name) {
        this.name = name;
    }

    public Identifier getIdentifier() {
        return Identifier.from(this.getName());
    }
}
