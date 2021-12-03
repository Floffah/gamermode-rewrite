package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * See information below Join Game (0x26) packet on the protocol wiki (https://wiki.vg/Protocol#Join_Game)
 */
public class BiomeEffectProperties {

    /**
     * The color of the sky.
     */
    public int sky_color;
    /**
     * Possible the tint color when swimming.
     */
    public int water_fog_color;
    /**
     * Possible the color of the fog when looking past the view distance.
     */
    public int fog_color;
    /**
     * The tint color of the water blocks.
     */
    public int water_color;

    /**
     * The tint color of the grass.
     */
    @Nullable
    public Integer foliage_color;

    /**
     * ?
     */
    @Nullable
    public Integer grass_color;

    /**
     * Unknown, likely affects foliage color.
     */
    @Nullable
    public BiomeEffectGrassColorModifier grass_color_modifier;

    /**
     * Music propertiers for the biome.
     */
    @Nullable
    public BiomeEffectMusicProperties music;

    /**
     * Ambient soundtrack.
     */
    @Nullable
    public Identifier ambient_sound;

    /**
     * Additional ambient sound that plays randomly.
     */
    @Nullable
    public BiomeEffectAdditionsSoundProperties additions_sound;

    /**
     * Additional ambient sound that plays at an interval.
     */
    @Nullable
    public BiomeEffectMoodSoundProperties mood_sound;
}
