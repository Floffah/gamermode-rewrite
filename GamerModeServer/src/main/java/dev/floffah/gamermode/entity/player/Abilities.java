package dev.floffah.gamermode.entity.player;

import dev.floffah.gamermode.datatype.BitMask;
import lombok.Getter;
import lombok.Setter;

public class Abilities {

    public static byte invulnerableField = 0x01;
    public static byte flyingField = 0x02;
    public static byte allowFlightField = 0x04;
    public static byte instantBreakingField = 0x08;

    /**
     * The represented player
     * -- GETTER --
     * Get the represented player
     *
     * @return The represented player
     */
    @Getter
    private final Player player;

    /**
     * Whether the player is damageable
     * -- GETTER --
     * Get whether the player is damageable
     *
     * @return Whether the player is damageable
     */
    @Getter
    @Setter
    private boolean invulnerable;

    /**
     * Whether the player is flying
     * -- GETTER --
     * Get whether the player is flying
     *
     * @return Whether the player is flying
     */
    @Getter
    @Setter
    private boolean flying;

    /**
     * Whether the player is allowed to fly
     * -- GETTER --
     * Get whether the player is allowed to fly
     *
     * @return Whether the player is allowed to fly
     */
    @Getter
    @Setter
    private boolean allowFlight;

    /**
     * Whether the player can instantly break blocks
     * -- GETTER --
     * Get whether the player can instantly break blocks
     *
     * @return Whether the player can instantly break blocks
     */
    @Getter
    @Setter
    private boolean instantBreaking;

    public Abilities(Player player) {
        this.player = player;
    }

    /**
     * Get the bitfield representing the abilities
     *
     * @return The bitfield representing the abilities
     */
    public int toBitField() {
        BitMask mask = new BitMask();

        mask.setBooleanAt(Abilities.invulnerableField, this.invulnerable);
        mask.setBooleanAt(Abilities.flyingField, this.flying);
        mask.setBooleanAt(Abilities.allowFlightField, this.allowFlight);
        mask.setBooleanAt(Abilities.instantBreakingField, this.instantBreaking);

        return mask.getMask();
    }

    /**
     * Set the abilities from a bit field.
     * Please note, this is only intended to be used when parsing the bitfield sent from the client via the serverbound Player Abilities 0x19 packet which only is sent when the flying state changes.
     *
     * @param bitmask          The bitfield
     * @param allowStartFlying Whether the player is allowed to fly
     */
    public void parse(int bitmask, boolean allowStartFlying) {
        BitMask mask = new BitMask(bitmask);

        boolean isNowFlying = mask.booleanAt(Abilities.flyingField);
        if (isNowFlying && !allowStartFlying) return;
        this.flying = isNowFlying;
    }
}
