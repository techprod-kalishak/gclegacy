package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class GaseousFluid extends BaseFlowingFluid {
    protected GaseousFluid(Properties properties) {
        super(properties);
    }

    public static FluidType gaseousType(String translationKey) {
        return new FluidType(FluidType.Properties.create()
                .descriptionId(translationKey)
                .canPushEntity(false)
                .canSwim(false)
                .canDrown(false));
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
        return true;
    }

    @Override
    protected boolean isSolidFace(BlockGetter level, BlockPos neighborPos, Direction side) {
        return false;
    }

    @Override
    protected void spread(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState) {

    }

    @Override
    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState) {

    }

    public static class Still extends GaseousFluid {
        public Still(Properties properties) {
            super(properties);
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }
    }

    public static class Flowing extends GaseousFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            builder.add(LEVEL);
        }
    }
}
