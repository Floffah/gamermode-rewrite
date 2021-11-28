package dev.floffah.gamermode.world;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class MiscWorldData {

    /**
     * The relating world
     * -- GETTER --
     * Get the relating world
     *
     * @return The relating world
     */
    @Getter
    protected World world;

    /**
     * Brands of server software that have interacted with the world
     * -- GETTER --
     * Get the list of brands of server software that have interacted with the world
     *
     * @return The list of brands of server software that have interacted with the world
     */
    @Getter
    protected List<String> serverBrands = new LinkedList<>();

    public MiscWorldData(World world) {
        this.world = world;

        serverBrands.add("GamerMode");
    }
}
