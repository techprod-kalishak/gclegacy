/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.NonNull;

public enum FeatureTier implements SerializableEnum {
    TIER_1("basic", "moon", 0, 1, 25, GalacticraftTags.Biomes.HAS_BASIC_FEATURES),
    TIER_2("advanced", "mars", 1, 2, 50, GalacticraftTags.Biomes.HAS_ADVANCED_FEATURES),
    TIER_3("ultimate", "venus", 2, 3, 75, GalacticraftTags.Biomes.HAS_ULTIMATE_FEATURES);

    public static final Codec<FeatureTier> CODEC = SerializableEnum.codec(FeatureTier.class);
    public static final StreamCodec<ByteBuf, FeatureTier> STREAM_CODEC = SerializableEnum.streamCodec(FeatureTier.class);
    private final String name;
    private final String celestialBodyName;
    private final int id;
    private final int level;
    private final int energyConsumptionBase;
    private final TagKey<Biome> availableIn;

    FeatureTier(String name, String celestialBodyName, int id, int level, int energyConsumptionBase, TagKey<Biome> availableIn) {
        this.name = name;
        this.celestialBodyName = celestialBodyName;
        this.id = id;
        this.level = level;
        this.energyConsumptionBase = energyConsumptionBase;
        this.availableIn = availableIn;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public @NonNull String getCelestialBodyName() {
        return this.celestialBodyName;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public String getSuffix() {
        return "_tier_" + this.level;
    }

    public int getEnergyConsumptionBase() {
        return this.energyConsumptionBase;
    }

    public TagKey<Biome> getAvailableIn() {
        return this.availableIn;
    }
}
