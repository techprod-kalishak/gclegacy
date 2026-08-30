/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractPadBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, SealableBlock {
    public static final EnumProperty<PadState> PAD_STATE = EnumProperty.create("pad_type", PadState.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape CENTER_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);

    public AbstractPadBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(PAD_STATE, PadState.NONE).setValue(WATERLOGGED, false));
    }

    @Override
    protected abstract MapCodec<? extends AbstractPadBlock> codec();

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Direction.stream()
                .filter(s -> s.getAxis().isHorizontal())
                .allMatch(side -> isSameAxis(level, pos, defaultBlockState(), side));
    }

    @Override
    public boolean isSealed(BlockGetter level, BlockPos pos, Direction facing) {
        return facing == Direction.UP;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PAD_STATE) == PadState.CENTER ? CENTER_SHAPE : SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PAD_STATE, WATERLOGGED);
    }

    protected static boolean isSameAxis(LevelReader level, BlockPos blockPos, BlockState block, Direction direction) {
        int count = 0;

        for (int i = 0; i < 4; i++) {
            if (level.getBlockState(blockPos.relative(direction)).is(block.typeHolder())) {
                count++;
            }
        }

        return count < 3;
    }
}
