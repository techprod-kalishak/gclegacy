/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.BlockWithDummies;
import io.kalishak.galacticraftlegacy.world.level.block.DummyBlock;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.SealableBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractSolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractSolarPanelBlock extends AbstractMachineBlock implements SealableBlock, BlockWithDummies {
    protected AbstractSolarPanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NonNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
            destroyDummyBlock(level, GalacticraftBlocks.DUMMY_SOLAR_PANEL, pos.above(), player);
            destroyDummyBlock(level, GalacticraftBlocks.DUMMY_SOLAR_PANEL, pos.above(2), player);

            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    destroyDummyBlock(level, GalacticraftBlocks.DUMMY_SOLAR_PANEL, pos.offset(x, 3, z), player);
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        BlockState dummyState = GalacticraftBlocks.DUMMY_SOLAR_PANEL.get().defaultBlockState();

        setDummyBlock(level, pos.above(), dummyState.setValue(DummyBlock.TYPE, DummyBlock.Type.SOLAR_PANEL_POLE));
        setDummyBlock(level, pos.above(2), dummyState.setValue(DummyBlock.TYPE, DummyBlock.Type.SOLAR_PANEL_POLE));

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                setDummyBlock(level, pos.offset(x, 3, z), dummyState.setValue(DummyBlock.TYPE, DummyBlock.Type.SOLAR_PANEL));
            }
        }
    }

    @Override
    public boolean isSealed(BlockGetter level, BlockPos pos, Direction facing) {
        return facing == Direction.UP;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();

        if (!level.getBlockState(above).isAir()) {
            return false;
        }

        if (level.getBlockState(above.above()).isAir()) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (!level.getBlockState(above.relative(direction)).isAir()) {
                    return false;
                }
            }
        }

        return super.canSurvive(state, level, pos);
    }

    protected static <T extends BlockEntity> @Nullable BlockEntityTicker<T> createTicker(Level level, BlockEntityType<T> actualType, BlockEntityType<? extends AbstractSolarPanelBlockEntity> expectedType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(actualType, expectedType, (_, pos, state, entity) -> AbstractSolarPanelBlockEntity.serverTick(serverLevel, pos, state, entity));
        }

        return null;
    }
}
