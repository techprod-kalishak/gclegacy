package io.kalishak.galacticraftlegacy.data.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public record CelestialBodyLevelData(ResourceKey<CelestialBodyInfo> bodyInfo) {
    public static final Codec<CelestialBodyLevelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO).fieldOf("celestial_body_info").forGetter(CelestialBodyLevelData::bodyInfo)
    ).apply(instance, CelestialBodyLevelData::new));

    public @Nullable CelestialBodyInfo get(HolderGetter<CelestialBodyInfo> holderGetter) {
        Holder<CelestialBodyInfo> orNull = holderGetter.getOrThrow(this.bodyInfo);

        return orNull.isBound() ? orNull.value() : null;
    }
}
