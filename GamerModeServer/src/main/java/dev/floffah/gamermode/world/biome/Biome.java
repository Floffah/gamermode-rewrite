package dev.floffah.gamermode.world.biome;

import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.world.biome.dessert.Plains;
import net.querz.nbt.tag.CompoundTag;

public interface Biome {
    Biome PLAINS = new Plains();

    public static CompoundTag buildBiomeElement(Biome biome) {
        // "": {
        BiomeProperties properties = biome.getProperties();
        CompoundTag element = new CompoundTag();

        element.putString("precipitation", properties.precipitation.getName());
        element.putFloat("depth", properties.depth);
        element.putFloat("temperature", properties.temperature);
        element.putFloat("scale", properties.scale);
        element.putFloat("downfall", properties.downfall);
        element.putString("category", properties.category.getName());
        if (properties.temperature_modifier != null) element.putString(
            "temperature_modifier",
            properties.temperature_modifier.getName()
        );

        // "effects": {
        BiomeEffectProperties effects = properties.effects;

        CompoundTag elementEffects = new CompoundTag();

        // int sky_color,
        // int water_fog_color,
        // int fog_color,
        // int water_color,
        // nullable int foliage_color,
        // nullable int grass_color,
        // nullable string grass_color_modifier (getName)
        elementEffects.putInt("sky_color", effects.sky_color);
        elementEffects.putInt("water_fog_color", effects.water_fog_color);
        elementEffects.putInt("fog_color", effects.fog_color);
        elementEffects.putInt("water_color", effects.water_color);
        if (effects.foliage_color != null) elementEffects.putInt(
            "foliage_color",
            effects.foliage_color
        );
        if (effects.grass_color != null) elementEffects.putInt(
            "grass_color",
            effects.grass_color
        );
        if (effects.grass_color_modifier != null) elementEffects.putString(
            "grass_color_modifier",
            effects.grass_color_modifier.getName()
        );

        // "music": {
        if (effects.music != null) {
            BiomeEffectMusicProperties effectMusic = effects.music;
            CompoundTag elementMusic = new CompoundTag();

            // boolean replace_current_music, string sound (toString), int max_delay, int min_delay
            elementMusic.putBoolean(
                "replace_current_music",
                effectMusic.replace_current_music
            );
            elementMusic.putString("sound", effectMusic.sound.toString());
            elementMusic.putInt("max_delay", effectMusic.max_delay);
            elementMusic.putInt("min_delay", effectMusic.min_delay);

            elementEffects.put("music", elementMusic);
        }
        // }

        if (effects.ambient_sound != null) elementEffects.putString(
            "ambient_sound",
            effects.ambient_sound.toString()
        );

        // "additions_sound": {
        if (effects.additions_sound != null) {
            BiomeEffectAdditionsSoundProperties effectAdditionsSound =
                effects.additions_sound;
            CompoundTag elementAdditionsSound = new CompoundTag();

            // string sound (toString), double tick_chance
            elementAdditionsSound.putString(
                "sound",
                effectAdditionsSound.sound.toString()
            );
            elementAdditionsSound.putDouble(
                "tick_chance",
                effectAdditionsSound.tick_chance
            );

            elementEffects.put("additions_sound", elementAdditionsSound);
        }
        // }

        // "mood_sound": {
        if (effects.mood_sound != null) {
            BiomeEffectMoodSoundProperties effectMoodSound = effects.mood_sound;
            CompoundTag elementMoodSound = new CompoundTag();

            // string sound (toString), int tick_delay, double offset, int block_search_extent
            elementMoodSound.putString(
                "sound",
                effectMoodSound.sound.toString()
            );
            elementMoodSound.putInt("tick_delay", effectMoodSound.tick_delay);
            elementMoodSound.putDouble("offset", effectMoodSound.offset);
            elementMoodSound.putInt(
                "block_search_extent",
                effectMoodSound.block_search_extent
            );

            elementEffects.put("mood_sound", elementMoodSound);
        }
        // }

        element.put("effects", elementEffects);
        // }

        // "particle": {
        if (properties.particle != null) {
            BiomeParticleProperties particle = properties.particle;
            CompoundTag elementParticle = new CompoundTag();

            elementParticle.putFloat("probability", particle.probability);

            // "options": {
            BiomeParticleOptionProperties particleOptions = particle.options;
            CompoundTag elementParticleOptions = new CompoundTag();

            elementParticleOptions.putString(
                "type",
                particleOptions.type.toString()
            );

            elementParticle.put("options", elementParticleOptions);
            // }

            element.put("particle", elementParticle);
        }
        // }

        // }
        return element;
    }

    /**
     * Get the biome's properties
     *
     * @return The biome's properties
     */
    public BiomeProperties getProperties();

    /**
     * Get the biome's name
     *
     * @return The biome's name
     */
    public Identifier getName();
}
