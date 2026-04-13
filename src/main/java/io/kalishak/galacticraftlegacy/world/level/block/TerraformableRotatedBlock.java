package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

public class TerraformableRotatedBlock extends TerraformableBlock {
    public static final MapCodec<TerraformableRotatedBlock> CODEC = simpleCodec(TerraformableRotatedBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public TerraformableRotatedBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(PERSISTENT, false).setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public @Nullable BlockState getTerraformedBlock(BlockGetter level, BlockPos blockPos) {
        return level.getBlockState(blockPos.above()).is(Tags.Blocks.GLASS_BLOCKS) ? Blocks.DIRT.defaultBlockState() : null;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return RotatedPillarBlock.rotatePillar(state, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT, AXIS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }
}
