package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

public interface FluidloggedBlock extends BucketPickup, LiquidBlockContainer {
    BooleanProperty FLUIDLOGGED = BooleanProperty.create("fluidlogged");

    default boolean canPlaceLiquid(@Nullable LivingEntity entity, BlockGetter blockGetter, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(FLUIDLOGGED);
    }

    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(FLUIDLOGGED)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(FLUIDLOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        }

        return false;
    }

    default ItemStack pickupBlock(@Nullable LivingEntity entity, LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (state.getValue(FLUIDLOGGED)) {
            levelAccessor.setBlock(pos, state.setValue(FLUIDLOGGED, false), Block.UPDATE_ALL);

            if (!state.canSurvive(levelAccessor, pos)) {
                levelAccessor.destroyBlock(pos, true);
            }

            FluidState fluidState = levelAccessor.getFluidState(pos);

            return fluidState.getType().getFluidType().getBucket(new FluidStack(fluidState.getType(), FluidType.BUCKET_VOLUME));
        }

        return ItemStack.EMPTY;
    }
}
