/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class LavaTurnableBlock extends Block {
    public static final MapCodec<LavaTurnableBlock> CODEC = simpleCodec(LavaTurnableBlock::new);

    public LavaTurnableBlock(Properties properties) {
        super(properties);
    }

    public static BlockState meltsInto() {
        return Blocks.LAVA.defaultBlockState();
    }

    @Override
    protected MapCodec<? extends LavaTurnableBlock> codec() {
        return CODEC;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);

        if (!EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_ICE_MELTING)) {
            BlockState belowState = level.getBlockState(pos.below());

            if (belowState.blocksMotion() || belowState.liquid()) {
                level.setBlockAndUpdate(pos, meltsInto());
            }
        }
    }
}
