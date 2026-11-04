/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.SealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public abstract class AbstractSolarPanelBlock extends AbstractMachineBlock implements SealableBlock {
    public static final EnumProperty<SolarPanelPart> PART = EnumProperty.create("solar_part", SolarPanelPart.class);
    protected static final VoxelShape SOLAR_PANEL_SHAPE = Block.box(0, 0, 0, 16, 3, 16);
    protected static final VoxelShape SOLAR_POLE_SHAPE = Block.box(4.8D, 0, 4.8D, 11.2D, 16, 11.2D);

    protected AbstractSolarPanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));//.setValue(PART, SolarPanelPart.BASE));
    }

//    @Override
//    protected @NonNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        return switch (state.getValue(PART)) {
//            case BASE -> super.getShape(state, level, pos, context);
//            case POLE -> SOLAR_POLE_SHAPE;
//            case PANEL -> SOLAR_PANEL_SHAPE;
//        };
//    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (state.getValue(PART) != SolarPanelPart.BASE) {
//            return InteractionResult.PASS;
//        }

        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider, pos);
            }
        }

        return InteractionResult.SUCCESS;
    }

//    @Override
//    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
//        switch (state.getValue(PART)) {
//            case BASE -> {
//                if (directionToNeighbour == Direction.UP && (!neighbourState.is(this) || neighbourState.getValue(PART) != SolarPanelPart.POLE)) {
//                    return Blocks.AIR.defaultBlockState();
//                }
//            }
//
//            case POLE -> {
//                if (directionToNeighbour == Direction.UP && (!neighbourState.is(this) || neighbourState.getValue(PART) != SolarPanelPart.PANEL)) {
//                    return Blocks.AIR.defaultBlockState();
//                }
//
//                if (directionToNeighbour == Direction.DOWN && (!neighbourState.is(this) || neighbourState.getValue(PART) != SolarPanelPart.BASE)) {
//                    return Blocks.AIR.defaultBlockState();
//                }
//            }
//
//            case PANEL -> {
//                if (directionToNeighbour == Direction.DOWN && (!neighbourState.is(this) || neighbourState.getValue(PART) != SolarPanelPart.POLE)) {
//                    return Blocks.AIR.defaultBlockState();
//                }
//            }
//        }
//
//        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
//    }

//    @Override
//    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
//        if (!level.isClientSide() && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
//            SolarPanelPart panelPart = state.getValue(PART);
//
//            if (panelPart != SolarPanelPart.BASE) {
//                BlockPos abovePos = pos.above();
//                BlockState aboveState = level.getBlockState(abovePos);
//
//                if (aboveState.is(state.getBlock()) && aboveState.getValue(PART) == SolarPanelPart.POLE) {
//                    BlockState blockState = Blocks.AIR.defaultBlockState();
//                    level.setBlock(abovePos, blockState, 35);
//                    level.levelEvent(player, LevelEvent.PARTICLES_AND_SOUND_DESTROY_BLOCK, abovePos, Block.getId(aboveState));
//                }
//
//                abovePos = abovePos.above();
//                aboveState = level.getBlockState(abovePos);
//                if (aboveState.is(state.getBlock()) && aboveState.getValue(PART) == SolarPanelPart.PANEL) {
//                    BlockState blockState = Blocks.AIR.defaultBlockState();
//                    level.setBlock(abovePos, blockState, 35);
//                    level.levelEvent(player, LevelEvent.PARTICLES_AND_SOUND_DESTROY_BLOCK, abovePos, Block.getId(aboveState));
//                }
//            }
//        }
//
//        return super.playerWillDestroy(level, pos, state, player);
//    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

//    @Override
//    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
//        level.setBlockAndUpdate(pos.above(), state.setValue(PART, SolarPanelPart.POLE));
//        level.setBlockAndUpdate(pos.above(2), state.setValue(PART, SolarPanelPart.PANEL));
//    }

    @Override
    public boolean isSealed(BlockGetter level, BlockPos pos, Direction facing) {
        return facing == Direction.UP;
    }

//    @Override
//    public @NonNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
//        SolarPanelPart solarPanelPart = state.getValue(PART);
//
//        if (solarPanelPart != SolarPanelPart.BASE) {
//            return ItemStack.EMPTY;
//        }
//
//        return super.getCloneItemStack(level, pos, state, includeData, player);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //builder.add(FACING, PART);
        builder.add(FACING);
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
}
