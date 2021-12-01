package dev.floffah.gamermode.world.dimension;

import dev.floffah.gamermode.datatype.Identifier;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class DimensionType {

    public static DimensionType OVERWORLD = new DimensionType(
        false,
        true,
        0.0f,
        null,
        Identifier.from("minecraft", "infiniburn_overworld"),
        false,
        true,
        true,
        Identifier.from("minecraft", "overworld"),
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

    @Nullable
    public Long fixed_time;

    public Identifier infiniburn;
    public boolean respawn_anchor_works;
    public boolean has_skylight;
    public boolean bed_works;
    public Identifier effects; // ?
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
        @Nullable Long fixed_time,
        Identifier infiniburn,
        boolean respawn_anchor_works,
        boolean has_skylight,
        boolean bed_works,
        Identifier effects,
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
