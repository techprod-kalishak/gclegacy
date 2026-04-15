/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum FabricatingBookCategory implements StringRepresentable {
    BASIC(0, "basic"),
    ADVANCED(1, "advanced"),
    SOLAR(2, "solar"),
    MISC(3, "misc");

    private static final IntFunction<FabricatingBookCategory> BY_ID = ByIdMap.continuous(category -> category.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<FabricatingBookCategory> CODEC = StringRepresentable.fromEnum(FabricatingBookCategory::values);
    public static final StreamCodec<ByteBuf, FabricatingBookCategory> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, category -> category.id);
    private final int id;
    private final String name;

    FabricatingBookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
