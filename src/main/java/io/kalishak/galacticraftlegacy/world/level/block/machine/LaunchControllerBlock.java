package io.kalishak.galacticraftlegacy.world.level.block.machine;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.savedata.TelemetryTracker;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LaunchControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class LaunchControllerBlock extends BaseEntityBlock {
    public static final MapCodec<LaunchControllerBlock> CODEC = simpleCodec(LaunchControllerBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public LaunchControllerBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<LaunchControllerBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LaunchControllerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        BlockEntity bentity = level.getBlockEntity(pos);

        if (bentity instanceof LaunchControllerBlockEntity launchController) {
            launchController.setLevelBoundedBlockPosition(level.dimension());

            if (level instanceof ServerLevel serverLevel) {
                TelemetryTracker tracker = serverLevel.getDataStorage().get(TelemetryTracker.SAVE_DATA_ID);

                if (tracker != null) {
                    tracker.addLaunchControllerAt(launchController.getLevelBoundedBlockPosition());
                }
            }
        }

        super.onPlace(state, level, pos, oldState, movedByPiston);
    }
}
