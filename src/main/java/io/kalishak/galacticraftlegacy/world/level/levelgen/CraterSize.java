/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import org.jspecify.annotations.NonNull;

public enum CraterSize implements StringRepresentable {
    SMALL("small", 8, 12, 14),
    MEDIUM("medium", 13, 17, 8),
    LARGE("large", 26, 30, 1);

    public static final Codec<CraterSize> CODEC = StringRepresentable.fromEnum(CraterSize::values);
    private final String name;
    private final int minSize;
    private final int maxSize;
    private final int chance;

    CraterSize(String name, int minSize, int maxSize, int chance) {
        this.name = name;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.chance = chance;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public int getMinSize() {
        return this.minSize;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public IntProvider asInt() {
        return UniformInt.of(this.minSize, this.maxSize);
    }

    public int getChance() {
        return this.chance;
    }
}
