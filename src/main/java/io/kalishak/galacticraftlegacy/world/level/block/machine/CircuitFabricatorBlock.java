/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CircuitFabricatorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CircuitFabricatorBlock extends AbstractMachineBlock {
    public static final MapCodec<CircuitFabricatorBlock> CODEC = simpleCodec(CircuitFabricatorBlock::new);

    public CircuitFabricatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<CircuitFabricatorBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, @Nullable BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof CircuitFabricatorBlockEntity circuitFabricator) {
            player.openMenu(circuitFabricator, pos);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CircuitFabricatorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    blockEntityType,
                    GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(),
                    (tickerLevel, tickerPos, tickerState, ticker) -> CircuitFabricatorBlockEntity.serverTick(serverLevel, tickerPos, tickerState, ticker)
            );
        }

        return null;
    }
}
