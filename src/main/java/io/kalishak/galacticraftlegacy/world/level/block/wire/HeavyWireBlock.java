/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.wire;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class HeavyWireBlock extends WireBlock {
    public HeavyWireBlock(double size, Properties properties) {
        super(size, properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;//new DenseWireBlockEntity(blockPos, blockState);
    }

//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState unlitState, BlockEntityType<T> blockEntityType) {
//        if (level instanceof ServerLevel serverLevel) {
//            return createTickerHelper(
//                    blockEntityType,
//                    GalacticraftBlockEntityType.DENSE_WIRE.get(),
//                    (l, wirePos, wireState, wire) -> WireBlockEntity.serverTick(serverLevel, wirePos, wireState, wire)
//            );
//        }
//
//        return null;
//    }
}
