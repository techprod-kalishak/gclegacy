/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public interface UnlitVariant {
    BlockState getLitState(Level level, BlockState unlitState);

    default InteractionResult litBlock(ItemStack stack, BlockState unlitState, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (stack.is(Tags.Items.TOOLS_IGNITER)) {
            if (!level.isClientSide() && OxygenHelper.hasOxygenNearby(level, pos, 1.0D, false)) {
                BlockState newState = getLitState(level, unlitState);

                stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, newState, UnlitTorchBlock.UPDATE_ALL);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}
