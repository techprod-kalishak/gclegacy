/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public abstract class PlanetaryTransition {
    public abstract boolean useParachute();

    public abstract BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player);
    public abstract BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity);
    public abstract @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player);

    public abstract void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket);

    public void setupAdventureModeSpawn(ServerPlayer player) {
    }

    public abstract MapCodec<? extends PlanetaryTransition> codec();
}
