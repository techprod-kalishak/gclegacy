/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SealedComparatorBlock extends FullDiodeBlock implements EntityBlock {
    public static final MapCodec<SealedComparatorBlock> CODEC = simpleCodec(SealedComparatorBlock::new);
    public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;

    public SealedComparatorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(MODE, ComparatorMode.COMPARE));
    }

    @Override
    protected MapCodec<SealedComparatorBlock> codec() {
        return CODEC;
    }

    @Override
    protected int getDelay(BlockState state) {
        return 2;
    }

    @Override
    protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof ComparatorBlockEntity comparator) {
            return comparator.getOutputSignal();
        }

        return 0;
    }

    private int calculateOutputSignal(Level level, BlockPos pos, BlockState state) {
        int inputSignal = getInputSignal(level, pos, state);

        if (inputSignal == 0) {
            return 0;
        }

        int alternateSignal = getAlternateSignal(level, pos, state);

        if (alternateSignal > inputSignal) {
            return 0;
        }

        return state.getValue(MODE) == ComparatorMode.SUBTRACT ? inputSignal - alternateSignal : inputSignal;
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        int input = getInputSignal(level, pos, state);

        if (input == 0) {
            return false;
        }

        int sideInput = getAlternateSignal(level, pos, state);

        return input > sideInput || input == sideInput && state.getValue(MODE) == ComparatorMode.COMPARE;
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        int resultSignal = super.getInputSignal(level, pos, state);

        Direction direction = state.getValue(FACING);
        BlockPos targetPos = pos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);

        if (targetState.hasAnalogOutputSignal()) {
            resultSignal = targetState.getAnalogOutputSignal(level, targetPos, direction.getOpposite());
        } else if (resultSignal < 15 && targetState.isRedstoneConductor(level, targetPos)) {
            targetPos = targetPos.relative(direction);
            targetState = level.getBlockState(targetPos);
            ItemFrame itemFrame = getItemFrame(level, direction, targetPos);
            int itemFrameOrBlockSignal = Math.max(itemFrame == null ? Integer.MIN_VALUE : itemFrame.getAnalogOutput(), targetState.hasAnalogOutputSignal() ? targetState.getAnalogOutputSignal(level, targetPos, direction.getOpposite()) : Integer.MIN_VALUE);

            if (itemFrameOrBlockSignal != Integer.MIN_VALUE) {
                resultSignal = itemFrameOrBlockSignal;
            }
        }

        return resultSignal;
    }

    private @Nullable ItemFrame getItemFrame(Level level, Direction direction, BlockPos tPos) {
        List<ItemFrame> itemFrames = level.getEntitiesOfClass(ItemFrame.class, new AABB(tPos.getX(), tPos.getY(), tPos.getZ(), tPos.getX() + 1, tPos.getY() + 1, tPos.getZ() + 1), (entity) -> entity.getDirection() == direction);

        return itemFrames.size() == 1 ? itemFrames.getFirst() : null;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!itemStack.is(GalacticraftTags.Items.WRENCH)) {
            return InteractionResult.PASS;
        }

        state = state.cycle(MODE);
        float pitch = state.getValue(MODE) == ComparatorMode.SUBTRACT ? 0.55F : 0.5F;
        level.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
        level.setBlock(pos, state, 2);

        if (level.getBlockState(pos).is(this)) {
            refreshOutputState(level, pos, state);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (level.getBlockTicks().willTickThisTick(pos, this)) return;

        int outputValue = calculateOutputSignal(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        int outputSignal = 0;

        if (blockEntity instanceof ComparatorBlockEntity comparator) {
            outputSignal = comparator.getOutputSignal();
        }

        int oldValue = outputSignal;

        if (outputValue != oldValue || state.getValue(POWERED) != shouldTurnOn(level, pos, state)) {
            TickPriority priority = shouldPrioritize(level, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
            level.scheduleTick(pos, this, 2, priority);
        }
    }

    private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
        int outputValue = this.calculateOutputSignal(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        int oldValue = 0;

        if (blockEntity instanceof ComparatorBlockEntity comparator) {
            oldValue = comparator.getOutputSignal();
            comparator.setOutputSignal(outputValue);
        }

        if (oldValue != outputValue || state.getValue(MODE) == ComparatorMode.COMPARE) {
            boolean sourceOn = this.shouldTurnOn(level, pos, state);
            boolean isOn = state.getValue(POWERED);

            if (isOn && !sourceOn) {
                level.setBlock(pos, state.setValue(POWERED, false), 2);
            } else if (!isOn && sourceOn) {
                level.setBlock(pos, state.setValue(POWERED, true), 2);
            }

            updateNeighborsInFront(level, pos, state);
        }

    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        refreshOutputState(level, pos, state);
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        super.triggerEvent(state, level, pos, b0, b1);

        BlockEntity blockEntity = level.getBlockEntity(pos);

        return blockEntity != null && blockEntity.triggerEvent(b0, b1);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ComparatorBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, MODE);
    }

    @Override
    public boolean getWeakChanges(BlockState state, LevelReader level, BlockPos pos) {
        return state.is(GalacticraftBlocks.SEALED_COMPARATOR);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader levelReader, BlockPos pos, BlockPos neighbor) {
        if (pos.getY() != neighbor.getY() || !(levelReader instanceof Level level)) return;

        if (!levelReader.isClientSide()) {
            state.handleNeighborChanged(level, pos, levelReader.getBlockState(neighbor).getBlock(), null, false);
        }
    }
}
