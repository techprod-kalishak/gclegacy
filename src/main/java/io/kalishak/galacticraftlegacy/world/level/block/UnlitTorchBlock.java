/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public class UnlitTorchBlock extends BaseTorchBlock implements UnlitVariant {
    public static final MapCodec<UnlitTorchBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("lit_state").forGetter(block -> block.litState),
            propertiesCodec()
    ).apply(instance, UnlitTorchBlock::new));
    protected final BlockState litState;

    public UnlitTorchBlock(BlockState litState, Properties properties) {
        super(properties);
        this.litState = litState;
    }

    @Override
    protected MapCodec<? extends UnlitTorchBlock> codec() {
        return CODEC;
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
