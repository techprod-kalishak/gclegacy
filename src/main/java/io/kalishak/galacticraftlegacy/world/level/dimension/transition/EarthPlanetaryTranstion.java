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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class EarthPlanetaryTranstion extends PlanetaryTransition {
    public static final EarthPlanetaryTranstion INSTANCE = new EarthPlanetaryTranstion();
    public static final MapCodec<EarthPlanetaryTranstion> CODEC = MapCodec.unit(EarthPlanetaryTranstion::new);

    @Override
    public boolean useParachute() {
        return true;
    }

    @Override
    public BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player) {
        return player.blockPosition().atY(250);
    }

    @Override
    public BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity) {
        return entity.blockPosition().atY(250);
    }

    @Override
    public @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
        RandomSource random = level.getRandom();
        double x = (random.nextDouble() * 2 - 1.0D) * 5.0D;
        double z = (random.nextDouble() * 2 - 1.0D) * 5.0D;

        return new BlockPos(
                player.getBlockX() + (int) Math.floor(x),
                250,
                player.getBlockZ() + (int) Math.floor(z)
        );
    }

    @Override
    public void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket) {

    }

    @Override
    public MapCodec<EarthPlanetaryTranstion> codec() {
        return CODEC;
    }
}
