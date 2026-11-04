/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AdvancedCompressorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.machine.CompressorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AdvancedCompressorBlock extends CompressorBlock {
    public AdvancedCompressorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedCompressorBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, GalacticraftBlockEntityTypes.ADVANCED_COMPRESSOR.get(), (_, pos, state, blockEntity) -> AdvancedCompressorBlockEntity.serverTick(serverLevel, pos, state, blockEntity))
                : null;
    }
}
