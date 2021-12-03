package dev.floffah.gamermode.world.biome.dessert;

import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.world.biome.*;
import lombok.Getter;

public class Plains implements Biome {

    @Getter
    private final BiomeProperties properties;

    @Getter
    private final Identifier name = Identifier.from("plains");

    public Plains() {
        this.properties = new BiomeProperties();
        this.properties.precipitation = BiomePrecipitation.RAIN;
        this.properties.depth = 0.125f;
        this.properties.temperature = 0.8f;
        this.properties.scale = 0.05f;
        this.properties.downfall = 0.4f;
        this.properties.category = BiomeCategory.PLAINS;

        BiomeEffectProperties effects = new BiomeEffectProperties();
        effects.sky_color = 7907327;
        effects.water_fog_color = 329011;
        effects.fog_color = 12638463;
        effects.water_color = 4159204;

        BiomeEffectMoodSoundProperties moodSound = new BiomeEffectMoodSoundProperties();
        moodSound.tick_delay = 6000;
        moodSound.offset = 2.0d;
        moodSound.sound = Identifier.from("minecraft", "ambient.cave");
        moodSound.block_search_extent = 8;

        effects.mood_sound = moodSound;

        this.properties.effects = effects;
    }
}
