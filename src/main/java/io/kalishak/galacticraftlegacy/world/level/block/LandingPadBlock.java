/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.LandingPadBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class LandingPadBlock extends AbstractPadBlock {
    public static final MapCodec<LandingPadBlock> MAP_CODEC = simpleCodec(LandingPadBlock::new);

    public LandingPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<LandingPadBlock> codec() {
        return MAP_CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LandingPadBlockEntity(blockPos, blockState);
    }
}
