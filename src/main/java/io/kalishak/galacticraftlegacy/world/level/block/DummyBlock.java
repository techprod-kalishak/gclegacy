package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.machine.electric.AbstractSolarPanelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DummyBlock extends Block {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("dummy_type", Type.class);
    protected static final VoxelShape SOLAR_PANEL_SHAPE = Block.box(0, 0, 0, 16, 3, 16);
    protected static final VoxelShape SOLAR_POLE_SHAPE = Block.box(4, 0, 4, 10, 16, 10);

    public DummyBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(TYPE, Type.WORKBENCH));
    }

    @Override
    protected @NonNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case SOLAR_PANEL -> SOLAR_PANEL_SHAPE;
            case SOLAR_PANEL_POLE -> SOLAR_POLE_SHAPE;
            default -> super.getShape(state, level, pos, context);
        };
    }

    @Override
    protected @NonNull VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected @NonNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        switch (state.getValue(TYPE)) {
            case SOLAR_PANEL_POLE -> {
                if (directionToNeighbour == Direction.DOWN && (!(neighbourState.getBlock() instanceof AbstractSolarPanelBlock) || !neighbourState.is(this))) {
                    return Blocks.AIR.defaultBlockState();
                }
            }

            case SOLAR_PANEL -> {
                BlockState stateBelow = level.getBlockState(neighbourPos.below());

                if (directionToNeighbour == Direction.DOWN && !(stateBelow.getBlock() instanceof AbstractSolarPanelBlock)) {
                    return Blocks.AIR.defaultBlockState();
                }
            }

            case WORKBENCH -> {
                if (directionToNeighbour == Direction.DOWN && !state.is(GalacticraftTags.BlockItems.NASA_WORKBENCHES.block())) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public @NonNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    public enum Type implements StringRepresentable {
        WORKBENCH("workbench"),
        SOLAR_PANEL_POLE("pole"),
        SOLAR_PANEL("panel");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }
    }
}
