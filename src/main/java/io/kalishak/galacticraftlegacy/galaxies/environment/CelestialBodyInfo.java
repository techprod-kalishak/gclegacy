/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.datamap.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.PlanetaryTransition;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.TransitionType;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * Immutable data regarding every space object. Do not have to be habitable by the Player
 * @param celestialObject celestial body reference
 * @param atmosphereInfo General information about the atmosphere of this celestial body
 * @param gravityScale Modifier used to modify entity's gravity attribute
 * @param transition Information about transitions to this celestial body
 */
public record CelestialBodyInfo(Holder<CelestialObject> celestialObject, AtmosphereInfo atmosphereInfo, float gravityScale, PlanetaryTransition transition, Optional<Float> fuelUsageMultiplier) {
    public static final Codec<CelestialBodyInfo> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CelestialObject.CODEC.fieldOf("celestial_object_reference").forGetter(CelestialBodyInfo::celestialObject),
            AtmosphereInfo.CODEC.fieldOf("atmosphere_info").forGetter(CelestialBodyInfo::atmosphereInfo),
            Codec.FLOAT.fieldOf("gravity_scale").forGetter(CelestialBodyInfo::gravityScale),
            TransitionType.CODEC.fieldOf("transition").forGetter(CelestialBodyInfo::transition),
            Codec.FLOAT.optionalFieldOf("fuel_usage_multiplier").forGetter(CelestialBodyInfo::fuelUsageMultiplier)
    ).apply(instance, CelestialBodyInfo::new));
    public static final Codec<Holder<CelestialBodyInfo>> CODEC = RegistryFileCodec.create(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CelestialBodyInfo>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO);

    public static boolean canLivingBreath(@Nullable CelestialBodyInfo celestialBodyData) {
        return celestialBodyData == null || celestialBodyData.atmosphereInfo.isBreathable();
    }

    public static boolean canLivingBreath(Level level) {
        return canLivingBreath(getFromLevel(level));
    }

    public static @Nullable CelestialBodyInfo getFromLevel(Level level) {
        CelestialBodyLevelData levelData = level.dimensionTypeRegistration().getData(GalacticraftDataMaps.CELESTIAL_BODY_DATA);

        if (levelData == null) {
            return null;
        }

        return levelData.get(level.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL_BODY_INFO));
    }
}
