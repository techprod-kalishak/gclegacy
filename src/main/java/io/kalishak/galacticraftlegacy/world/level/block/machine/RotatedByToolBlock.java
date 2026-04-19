/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface RotatedByToolBlock {
    default InteractionResult rotateWithWrench(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (stack.is(GalacticraftItems.WRENCH) && !stack.nextDamageWillBreak()) {
            Rotation rotation = player.isShiftKeyDown() ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;

            state = state.rotate(level, pos, rotation);
            level.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, new GameEvent.Context(player, state));

            stack.hurtAndBreak(1, player, hand);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}
