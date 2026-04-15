/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;

public class UnlitTorchBlock extends BaseTorchBlock {
    public static final MapCodec<UnlitTorchBlock> CODEC = simpleCodec(UnlitTorchBlock::new);

    public UnlitTorchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends UnlitTorchBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Tags.Items.TOOLS_IGNITER)) {
            if (!level.isClientSide() && OxygenHelper.hasOxygenNearby(level, pos, 1.0D, false)) {
                BlockState newState = Blocks.TORCH.defaultBlockState(); //todo possibility to turn them into Copper or Soul torches

                if (state.getBlock() instanceof WallUnlitTorchBlock) {
                    newState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, state.getValue(WallUnlitTorchBlock.FACING));
                }

                level.setBlock(pos, newState, UnlitTorchBlock.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return InteractionResult.CONSUME;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
