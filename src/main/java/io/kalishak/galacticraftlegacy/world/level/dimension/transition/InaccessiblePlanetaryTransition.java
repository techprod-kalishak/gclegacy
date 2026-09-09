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

public class InaccessiblePlanetaryTransition extends PlanetaryTransition {
    public static final InaccessiblePlanetaryTransition INSTANCE = new InaccessiblePlanetaryTransition();
    public static final MapCodec<InaccessiblePlanetaryTransition> MAP_CODEC = MapCodec.unit(InaccessiblePlanetaryTransition::new);

    private InaccessiblePlanetaryTransition() {}

    @Override
    public boolean useParachute() {
        return false;
    }

    @Override
    public BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player) {
        return BlockPos.ZERO;
    }

    @Override
    public BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity) {
        return BlockPos.ZERO;
    }

    @Override
    public @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
        return null;
    }

    @Override
    public void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket) {
    }

    @Override
    public MapCodec<InaccessiblePlanetaryTransition> codec() {
        return MAP_CODEC;
    }
}
