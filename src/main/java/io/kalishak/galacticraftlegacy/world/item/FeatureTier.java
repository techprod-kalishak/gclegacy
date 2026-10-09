/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;

public enum FeatureTier implements SerializableEnum {
    UNREACHABLE("unreachable", "sol", 0, 0),
    TIER_1("basic", "moon", 1, 1),
    TIER_2("advanced", "mars", 2, 2),
    TIER_3("ultimate", "venus", 3, 3);

    public static final Codec<FeatureTier> CODEC = SerializableEnum.codec(FeatureTier.class);
    public static final StreamCodec<ByteBuf, FeatureTier> STREAM_CODEC = SerializableEnum.streamCodec(FeatureTier.class);
    private final String name;
    private final String celestialBodyName;
    private final int id;
    private final int level;

    FeatureTier(String name, String celestialBodyName, int id, int level) {
        this.name = name;
        this.celestialBodyName = celestialBodyName;
        this.id = id;
        this.level = level;
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
}
