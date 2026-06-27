/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import io.kalishak.galacticraftlegacy.config.CommonConfig;
import io.kalishak.galacticraftlegacy.world.entity.LandingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class LanderPlanetaryTransition<E extends Entity> extends PlanetaryTransition {
    protected final EntityType<E> landerType;
    private final boolean landerDisabled;

    public LanderPlanetaryTransition(EntityType<E> landerType) {
        super(TransitionType.LANDER.get());
        this.landerType = landerType;
        this.landerDisabled = CommonConfig.DISABLE_LANDERS.get();
    }

    @Override
    public boolean useParachute() {
        return this.landerDisabled;
    }

    @Override
    public BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player) {
        return player.blockPosition().atY(this.landerDisabled ? 250 : 900);
    }

    @Override
    public BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity) {
        return entity.blockPosition().atY(this.landerDisabled ? 250 : 900);
    }

    @Override
    public @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
        if (this.landerDisabled) {
            RandomSource random = level.getRandom();
            double x = (random.nextDouble() * 2 - 1.0D) * 4.0D;
            double z = (random.nextDouble() * 2 - 1.0D) * 4.0D;

            return new BlockPos(
                    player.getBlockX() + (int) Math.floor(x),
                    220,
                    player.getBlockZ() + (int) Math.floor(z)
            );
        }

        return null;
    }

    @Override
    public void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket) {
        if (!isRidingAutoRocket && !this.landerDisabled) {
            Entity landerEntity = this.landerType.create(newLevel, EntitySpawnReason.SPAWN_ITEM_USE);

            if (!(landerEntity instanceof LandingEntity)) return;

            landerEntity.setPos(player.getX(), player.getY(), player.getZ());

            if (!newLevel.isClientSide()) {
                newLevel.addFreshEntity(landerEntity);
                player.startRiding(landerEntity);
            }
        }
    }
}
