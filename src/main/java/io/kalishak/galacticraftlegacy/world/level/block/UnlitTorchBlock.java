/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UnlitTorchBlock extends BaseTorchBlock implements UnlitVariant {
    protected final BlockState litState;

    public UnlitTorchBlock(BlockState litState, Properties properties) {
        super(properties);
        this.litState = litState;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return litBlock(stack, state, level, pos, player, hand);
    }

    @Override
    public BlockState getLitState(Level level, BlockState unlitState) {
        return this.litState;
    }
}
