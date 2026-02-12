package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class GaseousFluid extends BaseFlowingFluid {
    protected GaseousFluid(Properties properties) {
        super(properties);
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
