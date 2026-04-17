/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.values;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

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
    public String getSerializedName() {
        return this.name;
    }

    public boolean requireFlags() {
        return this == ADVENTURE;
    }
}
