/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.config.CommonConfig;
import io.kalishak.galacticraftlegacy.world.entity.LandingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.jspecify.annotations.Nullable;

public class LanderPlanetaryTransition extends PlanetaryTransition {
    public static final MapCodec<LanderPlanetaryTransition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("lander_type").forGetter(transition -> transition.landerType)
    ).apply(instance, LanderPlanetaryTransition::new));

    protected final Holder<EntityType<?>> landerType;
    private static boolean landerDisabled = false;

    public LanderPlanetaryTransition(Holder<EntityType<?>> landerType) {
        this.landerType = landerType;
    }

    @SubscribeEvent
    public static void onConfigReloaded(ModConfigEvent.Reloading event) {
        LanderPlanetaryTransition.landerDisabled = CommonConfig.DISABLE_LANDERS.get();
    }

    @Override
    public boolean useParachute() {
        return LanderPlanetaryTransition.landerDisabled;
    }

    @Override
    public BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player) {
        return player.blockPosition().atY(LanderPlanetaryTransition.landerDisabled ? 250 : 900);
    }

    @Override
    public BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity) {
        return entity.blockPosition().atY(LanderPlanetaryTransition.landerDisabled ? 250 : 900);
    }

    @Override
    public @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
        if (LanderPlanetaryTransition.landerDisabled) {
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
        if (!isRidingAutoRocket && !LanderPlanetaryTransition.landerDisabled) {
            Entity landerEntity = this.landerType.value().create(newLevel, EntitySpawnReason.EVENT);

            if (!(landerEntity instanceof LandingEntity)) return;

            landerEntity.setPos(player.getX(), player.getY(), player.getZ());

            if (!newLevel.isClientSide()) {
                newLevel.addFreshEntity(landerEntity);
                player.startRiding(landerEntity);
            }
        }
    }

    @Override
    public MapCodec<LanderPlanetaryTransition> codec() {
        return MAP_CODEC;
    }
}
