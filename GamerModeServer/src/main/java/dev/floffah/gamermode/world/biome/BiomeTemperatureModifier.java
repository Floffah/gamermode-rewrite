package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import lombok.Getter;

public enum BiomeTemperatureModifier {
    FROZEN("frozen");

    @Getter
    private String name;

    BiomeTemperatureModifier(String name) {
        this.name = name;
    }

    public Identifier getIdentifier() {
        return Identifier.from(this.getName());
    }
}
