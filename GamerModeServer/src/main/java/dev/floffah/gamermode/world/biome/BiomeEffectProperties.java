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
    public @Nullable Integer foliage_color;
    /**
     * ?
     */
    public @Nullable Integer grass_color;
    /**
     * Unknown, likely affects foliage color.
     */
    public @Nullable BiomeEffectGrassColorModifier grass_color_modifier;
    /**
     * Music propertiers for the biome.
     */
    public @Nullable BiomeEffectMusicProperties music;
    /**
     * Ambient soundtrack.
     */
    public @Nullable Identifier ambient_sound;
    /**
     * Additional ambient sound that plays randomly.
     */
    public @Nullable BiomeEffectAdditionsSoundProperties additions_sound;
    /**
     * Additional ambient sound that plays at an interval.
     */
    public @Nullable BiomeEffectMoodSoundProperties mood_sound;
}
