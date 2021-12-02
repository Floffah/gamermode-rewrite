package dev.floffah.gamermode.world.dimension;

import dev.floffah.gamermode.datatype.Identifier;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;

/**
 * See information below Join Game (0x26) packet on the protocol wiki (https://wiki.vg/Protocol#Join_Game)
 */
public class DimensionType {

    /**
     * The default dimension type for the overworld. When accessing this you should clone it.
     */
    public static DimensionType DEFAULT_OVERWORLD = new DimensionType(
        false,
        true,
        0.0f,
        Identifier.from("minecraft", "infiniburn_overworld"),
        false,
        true,
        true,
        Identifier.from("minecraft", "overworld"),
        null,
        true,
        0,
        256,
        256,
        1.0f,
        false,
        false
    );
    /**
     * The default dimension type for the overworld's caves. When accessing this you should clone it.
     */
    public static DimensionType DEFAULT_OVERWORLD_CAVES = new DimensionType(
        false,
        true,
        0.0f,
        Identifier.from("minecraft", "infiniburn_overworld"),
        false,
        true,
        true,
        Identifier.from("minecraft", "overworld"),
        null,
        true,
        0,
        256,
        256,
        1.0f,
        false,
        true
    );
    /**
     * The default dimension type for the nether. When accessing this you should clone it.
     */
    public static DimensionType DEFAULT_THE_NETHER = new DimensionType(
        true,
        false,
        0.1f,
        Identifier.from("minecraft", "infiniburn_nether"),
        true,
        false,
        false,
        Identifier.from("minecraft", "the_nether"),
        18000L,
        false,
        0,
        256,
        128,
        8.0f,
        true,
        true
    );
    /**
     * The default dimension type for the end. When accessing this you should clone it.
     */
    public static DimensionType DEFAULT_THE_END = new DimensionType(
        false,
        false,
        0.0f,
        Identifier.from("minecraft", "infiniburn_end"),
        false,
        false,
        false,
        Identifier.from("minecraft", "the_end"),
        6000L,
        true,
        0,
        256,
        256,
        1.0f,
        false,
        false
    );

    public boolean piglin_safe;
    public boolean natural;
    public float ambient_light;

    public Identifier infiniburn;
    public boolean respawn_anchor_works;
    public boolean has_skylight;
    public boolean bed_works;
    public Identifier effects; // ?

    @Nullable
    public Long fixed_time;

    public boolean has_raids;
    public int min_y;
    public int height;
    public int logical_height;
    public float coordinate_scale;
    public boolean ultrawarm;
    public boolean has_ceiling;

    // a constructor which accepts all class variables as parameters
    public DimensionType(
        boolean piglin_safe,
        boolean natural,
        float ambient_light,
        Identifier infiniburn,
        boolean respawn_anchor_works,
        boolean has_skylight,
        boolean bed_works,
        Identifier effects,
        @Nullable Long fixed_time,
        boolean has_raids,
        int min_y,
        int height,
        int logical_height,
        float coordinate_scale,
        boolean ultrawarm,
        boolean has_ceiling
    ) {
        this.piglin_safe = piglin_safe;
        this.natural = natural;
        this.ambient_light = ambient_light;
        this.fixed_time = fixed_time;
        this.infiniburn = infiniburn;
        this.respawn_anchor_works = respawn_anchor_works;
        this.has_skylight = has_skylight;
        this.bed_works = bed_works;
        this.effects = effects;
        this.has_raids = has_raids;
        this.min_y = min_y;
        this.height = height;
        this.logical_height = logical_height;
        this.coordinate_scale = coordinate_scale;
        this.ultrawarm = ultrawarm;
        this.has_ceiling = has_ceiling;
    }

    public CompoundTag toNBT() {
        CompoundTag dim = new CompoundTag();

        dim.putBoolean("piglin_safe", this.piglin_safe);
        dim.putBoolean("natural", this.natural);
        dim.putFloat("ambient_light", this.ambient_light);
        if (this.fixed_time != null) dim.putLong("fixed_time", this.fixed_time);
        dim.putString("infiniburn", this.infiniburn.toString());
        dim.putBoolean("respawn_anchor_works", this.respawn_anchor_works);
        dim.putBoolean("has_skylight", this.has_skylight);
        dim.putBoolean("bed_works", this.bed_works);
        dim.putString("effects", this.effects.toString());
        dim.putBoolean("has_raids", this.has_raids);
        dim.putInt("min_y", this.min_y);
        dim.putInt("height", this.height);
        dim.putInt("logical_height", this.logical_height);
        dim.putFloat("coordinate_scale", this.coordinate_scale);
        dim.putBoolean("ultrawarm", this.ultrawarm);
        dim.putBoolean("has_ceiling", this.has_ceiling);

        return dim;
    }
}
