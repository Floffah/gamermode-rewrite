package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import lombok.Getter;

public enum BiomePrecipitation {
    RAIN("rain"),
    SNOW("snow"),
    NONE("none");

    @Getter
    private final String name;

    BiomePrecipitation(String name) {
        this.name = name;
    }

    public Identifier getIdentifier() {
        return Identifier.from(this.name);
    }
}
