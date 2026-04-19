/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UnlitLanternBlock extends LanternBlock implements UnlitVariant {
    public static final MapCodec<UnlitLanternBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("lit_state").forGetter(block -> block.litState),
            propertiesCodec()
    ).apply(instance, UnlitLanternBlock::new));
    protected final BlockState litState;

    public UnlitLanternBlock(BlockState litState, Properties properties) {
        super(properties);
        this.litState = litState;
    }

    @Override
    public MapCodec<? extends UnlitLanternBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return litBlock(itemStack, state, level,  pos, player, hand);
    }

    @Override
    public BlockState getLitState(Level level, BlockState unlitState) {
        return this.litState.setValue(HANGING, unlitState.getValue(HANGING)).setValue(WATERLOGGED, unlitState.getValue(WATERLOGGED));
    }
}
