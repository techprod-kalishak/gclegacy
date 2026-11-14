package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FilledCauldronBlock extends AbstractCauldronBlock {
    private static final VoxelShape SHAPE_INSIDE = Block.column(12.0F, 4.0F, 15.0F);
    private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);

    public FilledCauldronBlock(CauldronInteraction.Dispatcher interactions, Properties properties) {
        super(properties, interactions);
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return 0.9375F;
    }

    @Override
    public boolean isFull(BlockState blockState) {
        return true;
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return FILLED_SHAPE;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return 3;
    }
}
