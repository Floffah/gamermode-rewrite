package dev.floffah.gamermode.world.biome;

import org.jetbrains.annotations.Nullable;

/**
 * See information below Join Game (0x26) packet on the protocol wiki (https://wiki.vg/Protocol#Join_Game)
 */
public class BiomeProperties {

    /**
     * The type of precipitation in the biome.
     */
    public BiomePrecipitation precipitation;
    /**
     * The depth factor of the biome. 1.5 >= depth >= -1.8.
     */
    public float depth;
    /**
     * The temperature factor of the biome. 2.02 >= temperature >= -0.5.
     */
    public float temperature;
    /**
     * ?. 1.225 >= scale >= 0.0.
     */
    public float scale;
    /**
     * ?. 1.0 >= downfall >= 0.0.
     */
    public float downfall;
    /**
     * The category of the biome.
     */
    public BiomeCategory category;

    /**
     * ?.
     */
    @Nullable
    public BiomeTemperatureModifier temperature_modifier;

    /**
     * Biome effects
     */
    public BiomeEffectProperties effects;

    /**
     * Particles that appear randomly in the biome.
     */
    @Nullable
    public BiomeParticleProperties particle;
}
