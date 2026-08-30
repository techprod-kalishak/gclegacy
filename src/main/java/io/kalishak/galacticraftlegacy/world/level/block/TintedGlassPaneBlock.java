package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

public class TintedGlassPaneBlock extends CrossCollisionTransparentBlock {
    public static final MapCodec<TintedGlassPaneBlock> MAP_CODEC = simpleCodec(TintedGlassPaneBlock::new);

    public TintedGlassPaneBlock(BlockBehaviour.Properties properties) {
        super(2.0F, 16.0F, 2.0F, 16.0F, 16.0F, properties);
        registerDefaultState(
                this.stateDefinition.any()
                        .setValue(NORTH, false)
                        .setValue(EAST, false)
                        .setValue(SOUTH, false)
                        .setValue(WEST, false)
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected MapCodec<TintedGlassPaneBlock> codec() {
        return MAP_CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        BlockState northState = level.getBlockState(north);
        BlockState southState = level.getBlockState(south);
        BlockState westState = level.getBlockState(west);
        BlockState eastState = level.getBlockState(east);

        return this.defaultBlockState()
                .setValue(NORTH, attachsTo(northState, northState.isFaceSturdy(level, north, Direction.SOUTH)))
                .setValue(SOUTH, attachsTo(southState, southState.isFaceSturdy(level, south, Direction.NORTH)))
                .setValue(WEST, attachsTo(westState, westState.isFaceSturdy(level, west, Direction.EAST)))
                .setValue(EAST, attachsTo(eastState, eastState.isFaceSturdy(level, east, Direction.WEST)))
                .setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return directionToNeighbour.getAxis().isHorizontal()
                ? state.setValue(PROPERTY_BY_DIRECTION.get(directionToNeighbour), attachsTo(neighbourState, neighbourState.isFaceSturdy(level, neighbourPos, directionToNeighbour.getOpposite())))
                : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return false;
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        if (neighborState.is(this) || neighborState.is(Tags.Blocks.GLASS_PANES) && state.is(Tags.Blocks.GLASS_PANES) && neighborState.hasProperty(PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) {
            if (!direction.getAxis().isHorizontal()) {
                return true;
            }

            if (state.getValue(PROPERTY_BY_DIRECTION.get(direction)) && neighborState.getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) {
                return true;
            }
        }

        return super.skipRendering(state, neighborState, direction);
    }

    @Override
    protected int getLightDampening(BlockState state) {
        return 15;
    }

    public final boolean attachsTo(BlockState state, boolean faceSolid) {
        return !isExceptionForConnection(state) && faceSolid || state.is(GalacticraftTags.Blocks.CONNECTS_TO_TINTED_GLASS_PANES);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}
