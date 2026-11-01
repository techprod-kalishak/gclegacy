/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.values;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum GameDifficulty implements StringRepresentable {
    NORMAL("normal"),
    QUICK("quick"),
    HARD("hard"),
    ADVENTURE("adventure");

    public static final Codec<GameDifficulty> CODEC = StringRepresentable.fromValues(GameDifficulty::values);
    private final String name;

    GameDifficulty(String name) {
        this.name = name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }
}
