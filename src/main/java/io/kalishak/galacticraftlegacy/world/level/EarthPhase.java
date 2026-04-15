package io.kalishak.galacticraftlegacy.world.level;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import org.jspecify.annotations.NonNull;

public enum EarthPhase implements SerializableEnum {
    FULL_EARTH(0, "full_earth"),
    GIBBOUS(1, "gibbous"),
    LOW_ILLUMINATION_GIBBOUS(2, "low_illumination_gibbous"),
    VERY_LOW_ILLUMINATION_GIBBOUS(3, "very_low_illumination_gibbous"),
    HALF(4, "half"),
    HIGH_ILLUMINATION_CRESCENT(5, "high_illumination_crescent"),
    CRESCENT(6, "crescent"),
    LOW_ILLUMINATION_CRESCENT(7, "low_illumination_crescent");

    public static final Codec<EarthPhase> CODEC = SerializableEnum.codec(EarthPhase.class);
    public static final int PHASE_LENGTH = 24000;
    private final int id;
    private final String name;

    EarthPhase(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public int startTick() {
        return this.id * PHASE_LENGTH;
    }
}
