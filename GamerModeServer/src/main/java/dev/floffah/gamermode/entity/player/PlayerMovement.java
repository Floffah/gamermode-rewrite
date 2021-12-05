package dev.floffah.gamermode.entity.player;

import dev.floffah.gamermode.entity.EntityMovement;
import lombok.Getter;

public class PlayerMovement implements EntityMovement {

    @Getter
    private Player entity;

    /**
     * The player's flight speed
     * -- GETTER --
     * Get the player's flight speed
     *
     * @return The player's flight speed
     */
    @Getter
    private float flightSpeed = 0.05f;

    public PlayerMovement(Player entity) {
        this.entity = entity;
    }

    public float getFieldOfViewModifier() {
        // TODO: recognise field of view modifiers;
        return 0.1f;
    }
}
