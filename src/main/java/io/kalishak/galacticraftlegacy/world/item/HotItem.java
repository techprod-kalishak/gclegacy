/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FallenMeteorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public interface HotItem {
    default void touchingHurts(ItemStack itemStack, ServerLevel level, Entity holder, @Nullable EquipmentSlot slot) {
        HotContent hotContent = itemStack.get(GalacticraftDataComponents.HOT_CONTENT);

        if (hotContent != null && hotContent.decrease(itemStack)) {
            float scaledHeatLevel = hotContent.getScaledHeatLevel();

            if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                holder.hurtServer(level, level.damageSources().onFire(), scaledHeatLevel * 2.0F);
            }
        }
    }

    static void blockHurt(ServerLevel level, BlockPos worldPosition, Entity victim) {
        BlockEntity blockEntity = level.getBlockEntity(worldPosition);

        if (blockEntity instanceof FallenMeteorBlockEntity fallenMeteor) {
            HotContent content = fallenMeteor.getData(GalacticraftAttachments.HOT_CONTENT);

            if (content.getHeatLevel() > 0) {
                RandomSource randomSource = level.getRandom();

                level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F, 2.6F + (randomSource.nextFloat() - randomSource.nextFloat() * 0.8F));

                for (int i = 0; i < 8; i++) {
                    double x = worldPosition.getX() + randomSource.nextFloat();
                    double y = worldPosition.getY() + 0.2D + randomSource.nextFloat();
                    double z = worldPosition.getZ() + randomSource.nextFloat();

                    level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
                }

                if (!victim.isOnFire()) {
                    victim.setRemainingFireTicks(2);
                }

                victim.push(0.5D + randomSource.nextFloat() * 0.4D, 1.0D + randomSource.nextFloat() * 0.4D, 0.5D + randomSource.nextFloat() * 0.4D);
            }
        }
    }
}
