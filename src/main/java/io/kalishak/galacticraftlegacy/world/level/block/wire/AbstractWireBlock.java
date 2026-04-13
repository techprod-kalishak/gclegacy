package io.kalishak.galacticraftlegacy.world.level.block.wire;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.TransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWireBlock extends BaseEntityBlock implements ConnectingBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Util.makeEnumMap(Direction.class, face -> switch (face) {
        case UP -> UP;
        case DOWN -> DOWN;
        case NORTH -> NORTH;
        case EAST -> EAST;
        case SOUTH -> SOUTH;
        case WEST -> WEST;
    });

    protected final NetworkType networkType;
    protected final double size;
    protected final Map<BlockState, VoxelShape> shapes = new HashMap<>();

    public AbstractWireBlock(NetworkType networkType, double size, Properties properties) {
        super(properties);
        this.networkType = networkType;
        this.size = size;

        registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
        );

        this.stateDefinition.getPossibleStates().forEach(state -> shapes.put(state, createShapeForState(state, this.size)));
    }

    @Override
    protected abstract MapCodec<? extends AbstractWireBlock> codec();

    @Override
    public @NonNull NetworkType getNetworkType(@NonNull Direction direction) {
        return this.networkType;
    }

    public double getSize() {
        return this.size;
    }

    protected final void update(Level level, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            update(level, pos, state, direction);
        }

        if (level.getBlockEntity(pos) instanceof TransmitterBlockEntity transmitter) {
            transmitter.updateNeighbouringTransmitters(level, pos);
        }
    }

    protected final void update(Level level, BlockPos pos, BlockState state, Direction neighbourAt) {
        BooleanProperty connection = PROPERTY_BY_DIRECTION.get(neighbourAt);
        boolean canConnect = canBeConnected(level, pos, neighbourAt);

        level.setBlock(pos, state.setValue(connection, canConnect), Block.UPDATE_ALL);
    }

    protected boolean canBeConnected(Level level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);

        return neighborState.getBlock() instanceof ConnectingBlock connectingBlock && getNetworkType(direction) == connectingBlock.getNetworkType(direction.getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapes.getOrDefault(state, Shapes.block());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (level instanceof ServerLevel serverLevel) {
            update(serverLevel, pos, state, direction);

            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof TransmitterBlockEntity transmitter) {
                transmitter.updateNeighbouringTransmitters(serverLevel, pos);
            }
        }

        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = level.getFluidState(blockPos);

        return defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        update(level, pos, state);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    protected static VoxelShape createShapeForState(BlockState state, double size) {
        VoxelShape shape = Shapes.box(size, size, size, 1 - size, 1 - size, 1 - size);

        if (state.getValue(UP)) {
            shape = Shapes.or(shape, Shapes.box(size, size, size, 1 - size, 1, 1 - size));
        }
        if (state.getValue(DOWN)) {
            shape = Shapes.or(shape, Shapes.box(size, 0, size, 1 - size, 1 - size, 1 - size));
        }
        if (state.getValue(NORTH)) {
            shape = Shapes.or(shape, Shapes.box(size, size, 0, 1 - size, 1 - size, 1 - size));
        }
        if (state.getValue(EAST)) {
            shape = Shapes.or(shape, Shapes.box(size, size, size, 1, 1 - size, 1 - size));
        }
        if (state.getValue(SOUTH)) {
            shape = Shapes.or(shape, Shapes.box(size, size, size, 1 - size, 1 - size, 1));
        }
        if (state.getValue(WEST)) {
            shape = Shapes.or(shape, Shapes.box(0, size, size, 1 - size, 1 - size, 1 - size));
        }

        return shape;
    }
}
