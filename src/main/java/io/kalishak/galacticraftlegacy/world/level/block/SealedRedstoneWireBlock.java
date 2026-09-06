package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.world.level.redstone.DefaultSealedRedstoneEvaluator;
import io.kalishak.galacticraftlegacy.world.level.redstone.ExperimentalSealedRedstoneWireEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class SealedRedstoneWireBlock extends Block {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private final DefaultSealedRedstoneEvaluator evaluator = new DefaultSealedRedstoneEvaluator(this);
    private boolean shouldSignal = true;

    public SealedRedstoneWireBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
    }

    private void updatePowerStrength(Level level, BlockPos pos, BlockState state, @Nullable Orientation orientation, boolean shapeUpdateWiresAroundInitialPosition) {
        if (useExperimentalEvaluator(level)) {
            (new ExperimentalSealedRedstoneWireEvaluator(this)).updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
        } else {
            this.evaluator.updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
        }
    }

    public int getBlockSignal(Level level, BlockPos pos) {
        this.shouldSignal = false;
        int blockSignal = level.getBestNeighborSignal(pos);
        this.shouldSignal = true;

        return blockSignal;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock()) && !level.isClientSide()) {
            updatePowerStrength(level, pos, state, null, true);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (!movedByPiston) {
            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }

            updatePowerStrength(level, pos, state, null, false);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && (block != this || !useExperimentalEvaluator(level))) {
            updatePowerStrength(level, pos, state, orientation, false);
        }
    }

    private static boolean useExperimentalEvaluator(Level level) {
        return level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS);
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return !this.shouldSignal ? 0 : state.getSignal(level, pos, direction);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (this.shouldSignal && direction != Direction.DOWN) {
            return this.ownSignal(state, level, pos);
        }

        return 0;
    }

    @Override
    protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(POWER);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return this.shouldSignal;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }
}
