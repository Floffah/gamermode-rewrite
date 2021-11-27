package dev.floffah.gamermode.world;

import lombok.Getter;

public enum WorldType {
    OVERWORLD("minecraft:overworld"),
    NETHER("minecraft:nether"),
    END("minecraft:the_end");

    /**
     * The identifier of the world type.
     * -- GETTER --
     * Get the identifier of the world type.
     *
     * @return The identifier of the world type.
     */
    @Getter
    private String name;

    WorldType(String name) {
        this.name = name;
    }
}
