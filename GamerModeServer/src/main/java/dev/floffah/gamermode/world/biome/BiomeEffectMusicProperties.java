package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;

/**
 * See information below Join Game (0x26) packet on the protocol wiki (https://wiki.vg/Protocol#Join_Game)
 */
public class BiomeEffectMusicProperties {
    public boolean replace_current_music;
    public Identifier sound;
    public int max_delay;
    public int min_delay;
}
