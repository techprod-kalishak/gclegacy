/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractElectricFurnaceBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.ElectricArcFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArcFurnaceBlock extends AbstractMachineBlock {
    public ArcFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ElectricArcFurnaceBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    blockEntityType,
                    GalacticraftBlockEntityTypes.ELECTRIC_ARC_FURNACE.get(),
                    (_, tickerPos, tickerState, ticker) -> AbstractElectricFurnaceBlockEntity.serverTick(serverLevel, tickerPos, tickerState, ticker)
            );
        }

        return null;
    }
}
