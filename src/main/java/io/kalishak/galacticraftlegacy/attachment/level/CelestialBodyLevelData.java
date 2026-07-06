/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.level;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.registry.CelestialBodyLevelDataEntries;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.galaxies.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensions;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.PlanetaryTransition;
import io.kalishak.galacticraftlegacy.world.level.dimension.transition.TransitionType;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.Map;

/**
 * Immutable data regarding every space object. Do not have to be habitable by the Player
 * @param celestialObject celestial body reference
 * @param atmosphereInfo General information about the atmosphere of this celestial body
 * @param gravityScale Modifier used to modify entity's gravity attribute
 * @param transition Information about transitions to this celestial body
 */
public record CelestialBodyLevelData(Holder<CelestialObject> celestialObject, AtmosphereInfo atmosphereInfo, float gravityScale, PlanetaryTransition transition) {
    private static final Map<ResourceKey<Level>, ResourceKey<CelestialBodyLevelData>> KEYS = ImmutableMap.<ResourceKey<Level>, ResourceKey<CelestialBodyLevelData>>builder()
            .put(Level.OVERWORLD, CelestialBodyLevelDataEntries.OVERWORLD)
            .put(GalacticraftDimensions.MOON, CelestialBodyLevelDataEntries.MOON)
            .put(GalacticraftDimensions.EARTH_ORBIT, CelestialBodyLevelDataEntries.EARTH_ORBIT)
            .put(GalacticraftDimensions.MARS, CelestialBodyLevelDataEntries.MARS)
            .put(GalacticraftDimensions.VENUS, CelestialBodyLevelDataEntries.VENUS)
            .put(GalacticraftDimensions.ASTEROIDS, CelestialBodyLevelDataEntries.ASTEROIDS)
            .build();

    public static final Codec<CelestialBodyLevelData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CelestialObject.CODEC.fieldOf("celestial_object_reference").forGetter(CelestialBodyLevelData::celestialObject),
            AtmosphereInfo.CODEC.fieldOf("atmosphere_info").forGetter(CelestialBodyLevelData::atmosphereInfo),
            Codec.FLOAT.fieldOf("gravity_scale").forGetter(CelestialBodyLevelData::gravityScale),
            TransitionType.CODEC.fieldOf("transition").forGetter(CelestialBodyLevelData::transition)
    ).apply(instance, CelestialBodyLevelData::new));
    public static final Codec<Holder<CelestialBodyLevelData>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA);
    public static final StreamCodec<RegistryFriendlyByteBuf, CelestialBodyLevelData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            CelestialObject.STREAM_CODEC, CelestialBodyLevelData::celestialObject,
            AtmosphereInfo.STREAM_CODEC, CelestialBodyLevelData::atmosphereInfo,
            ByteBufCodecs.FLOAT, CelestialBodyLevelData::gravityScale,
            TransitionType.STREAM_CODEC, CelestialBodyLevelData::transition,
            CelestialBodyLevelData::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CelestialBodyLevelData>> STREAM_CODEC = ByteBufCodecs.holder(
            GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA,
            DIRECT_STREAM_CODEC
    );

    public static Holder<CelestialBodyLevelData> fromLevel(IAttachmentHolder attachmentHolder) {
        if (attachmentHolder instanceof Level level) {
            return level.registryAccess().getOrThrow(KEYS.get(level.dimension()));
        } else {
            throw new IllegalArgumentException("AttachmentHolder is not a Level!");
        }
    }
}
