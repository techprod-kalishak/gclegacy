/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FuelingPadBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class FuelingPadBlock extends AbstractPadBlock {
    public static final MapCodec<FuelingPadBlock> MAP_CODEC = simpleCodec(FuelingPadBlock::new);

    public FuelingPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<FuelingPadBlock> codec() {
        return MAP_CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FuelingPadBlockEntity(blockPos, blockState);
    }
}
