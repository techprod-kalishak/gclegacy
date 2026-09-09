/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.redstone;

import io.kalishak.galacticraftlegacy.world.level.block.SealedRedstoneWireBlock;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.debug.DebugSubscriptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class ExperimentalSealedRedstoneWireEvaluator extends SealedRedstoneEvaluator {
    private final Deque<BlockPos> wiresToTurnOff = new ArrayDeque<>();
    private final Deque<BlockPos> wiresToTurnOn = new ArrayDeque<>();
    private final Object2IntMap<BlockPos> updatedWires = new Object2IntLinkedOpenHashMap<>();

    public ExperimentalSealedRedstoneWireEvaluator(SealedRedstoneWireBlock sealedWireBlock) {
        super(sealedWireBlock);
    }

    @Override
    public void updatePowerStrength(Level level, BlockPos initialPos, BlockState ignored, @Nullable Orientation orientation, boolean includeVerticalSides) {
        Orientation initialOrientation = getInitialOrientation(level, orientation);
        calculateCurrentChanges(level, initialPos, initialOrientation);
        ObjectIterator<Object2IntMap.Entry<BlockPos>> iterator = this.updatedWires.object2IntEntrySet().iterator();

        for (boolean initialWire = true; iterator.hasNext(); initialWire = false) {
            Object2IntMap.Entry<BlockPos> next = iterator.next();
            BlockPos pos = next.getKey();
            int packed = next.getIntValue();
            int newLevel = unpackPower(packed);
            BlockState state = level.getBlockState(pos);

            if (state.is(this.sealedWireBlock) && !state.getValue(RedStoneWireBlock.POWER).equals(newLevel)) {
                int updateFlags = 2;

                if (!initialWire) {
                    updateFlags |= 128;
                }

                level.setBlock(pos, state.setValue(RedStoneWireBlock.POWER, newLevel), updateFlags);
            } else {
                iterator.remove();
            }
        }

        causeNeighborUpdates(level);
    }

    private void causeNeighborUpdates(Level level) {
        this.updatedWires.forEach((wirePos, packed) -> {
            Orientation orientation = unpackOrientation(packed);
            BlockState state = level.getBlockState(wirePos);

            for(Direction neighborDirection : orientation.getDirections()) {
                if (isConnected(state, neighborDirection)) {
                    BlockPos neighborPos = wirePos.relative(neighborDirection);
                    BlockState neighborState = level.getBlockState(neighborPos);
                    Orientation neighborOrientation = orientation.withFrontPreserveUp(neighborDirection);
                    level.neighborChanged(neighborState, neighborPos, this.sealedWireBlock, neighborOrientation, false);

                    if (neighborState.isRedstoneConductor(level, neighborPos)) {
                        for(Direction direction : neighborOrientation.getDirections()) {
                            if (direction != neighborDirection.getOpposite()) {
                                level.neighborChanged(neighborPos.relative(direction), this.sealedWireBlock, neighborOrientation.withFrontPreserveUp(direction));
                            }
                        }
                    }
                }
            }

        });
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.debugSynchronizers().hasAnySubscriberFor(DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS)) {
                this.updatedWires.forEach((wirePos, packed) -> serverLevel.debugSynchronizers().sendBlockValue(wirePos, DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, unpackOrientation(packed)));
            }
        }
    }

    private static boolean isConnected(BlockState state, Direction direction) {
        if (!(state.getBlock() instanceof RedStoneWireBlock)) return false;

        EnumProperty<RedstoneSide> property = RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(direction);
        return property == null ? direction == Direction.DOWN : state.getValue(property).isConnected();
    }

    private static Orientation getInitialOrientation(Level level, @Nullable Orientation incomingOrigination) {
        Orientation orientation = Objects.requireNonNullElseGet(incomingOrigination, () -> Orientation.random(level.getRandom()));

        return orientation.withUp(Direction.UP).withSideBias(Orientation.SideBias.LEFT);
    }

    private void calculateCurrentChanges(Level level, BlockPos initialPosition, Orientation initialOrientation) {
        BlockState initialState = level.getBlockState(initialPosition);

        if (initialState.is(this.sealedWireBlock)) {
            setPower(initialPosition, initialState.getValue(RedStoneWireBlock.POWER), initialOrientation);
            this.wiresToTurnOff.add(initialPosition);
        } else {
            propagateChangeToNeighbors(level, initialPosition, 0, initialOrientation, true);
        }

        BlockPos pos;
        Orientation orientation;
        int oldPower;
        int newPower;
        int powerToSet;

        for(; !this.wiresToTurnOff.isEmpty(); propagateChangeToNeighbors(level, pos, powerToSet, orientation, oldPower > newPower)) {
            pos = this.wiresToTurnOff.removeFirst();
            int packed = this.updatedWires.getInt(pos);
            orientation = unpackOrientation(packed);
            oldPower = unpackPower(packed);
            int blockPower = this.getBlockSignal(level, pos);
            int wirePower = this.getIncomingWireSignal(level, pos, true);
            newPower = Math.max(blockPower, wirePower);

            if (newPower < oldPower) {
                if (blockPower > 0 && !this.wiresToTurnOn.contains(pos)) {
                    this.wiresToTurnOn.add(pos);
                }

                powerToSet = 0;
            } else {
                powerToSet = newPower;
            }

            if (powerToSet != oldPower) {
                setPower(pos, powerToSet, orientation);
            }
        }

        for(; !this.wiresToTurnOn.isEmpty(); propagateChangeToNeighbors(level, pos, newPower, orientation, false)) {
            pos = this.wiresToTurnOn.removeFirst();
            int packed = this.updatedWires.getInt(pos);
            oldPower = unpackPower(packed);
            oldPower = getBlockSignal(level, pos);
            int wirePower = getIncomingWireSignal(level, pos, true);
            newPower = Math.max(oldPower, wirePower);
            orientation = unpackOrientation(packed);

            if (newPower > oldPower) {
                setPower(pos, newPower, orientation);
            } else if (newPower < oldPower) {
                throw new IllegalStateException("Turning off wire while trying to turn it on. Should not happen.");
            }
        }
    }

    private static int packOrientationAndPower(Orientation orientation, int power) {
        return orientation.getIndex() << 4 | power;
    }

    private static Orientation unpackOrientation(int packed) {
        return Orientation.fromIndex(packed >> 4);
    }

    private static int unpackPower(int packed) {
        return packed & 15;
    }

    private void setPower(BlockPos pos, int newPower, Orientation orientation) {
        this.updatedWires.compute(pos, (_, packed) -> packed == null ? packOrientationAndPower(orientation, newPower) : packOrientationAndPower(unpackOrientation(packed), newPower));
    }

    private void propagateChangeToNeighbors(Level level, BlockPos pos, int newPower, Orientation orientation, boolean allowTurningOff) {
        for(Direction directionHorizontal : orientation.getHorizontalDirections()) {
            BlockPos offsetPos = pos.relative(directionHorizontal);
            enqueueNeighborWire(level, offsetPos, newPower, orientation.withFront(directionHorizontal), allowTurningOff);
        }

        for(Direction directionVertical : orientation.getVerticalDirections()) {
            BlockPos offsetPos = pos.relative(directionVertical);
            boolean solidBlock = level.getBlockState(offsetPos).isRedstoneConductor(level, offsetPos);

            for(Direction directionHorizontal : orientation.getHorizontalDirections()) {
                BlockPos neighbor = pos.relative(directionHorizontal);

                if (directionVertical == Direction.UP && !solidBlock) {
                    BlockPos neighborWire = offsetPos.relative(directionHorizontal);
                    enqueueNeighborWire(level, neighborWire, newPower, orientation.withFront(directionHorizontal), allowTurningOff);
                } else if (directionVertical == Direction.DOWN && !level.getBlockState(neighbor).isRedstoneConductor(level, neighbor)) {
                    BlockPos neighborWire = offsetPos.relative(directionHorizontal);
                    enqueueNeighborWire(level, neighborWire, newPower, orientation.withFront(directionHorizontal), allowTurningOff);
                }
            }
        }
    }

    private void enqueueNeighborWire(Level level, BlockPos pos, int newFromPower, Orientation orientation, boolean allowTurningOff) {
        BlockState state = level.getBlockState(pos);
        if (state.is(this.sealedWireBlock)) {
            int toPower = this.getWireSignal(pos, state);

            if (toPower < newFromPower - 1 && !this.wiresToTurnOn.contains(pos)) {
                this.wiresToTurnOn.add(pos);
                this.setPower(pos, toPower, orientation);
            }

            if (allowTurningOff && toPower > newFromPower && !this.wiresToTurnOff.contains(pos)) {
                this.wiresToTurnOff.add(pos);
                this.setPower(pos, toPower, orientation);
            }
        }

    }

    protected int getWireSignal(BlockPos pos, BlockState state) {
        int packed = this.updatedWires.getOrDefault(pos, -1);
        return packed != -1 ? unpackPower(packed) : super.getWireSignal(pos, state);
    }
}
