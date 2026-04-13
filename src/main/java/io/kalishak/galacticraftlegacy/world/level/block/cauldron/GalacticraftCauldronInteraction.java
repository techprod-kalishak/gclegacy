package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class GalacticraftCauldronInteraction {
    public static final CauldronInteraction.InteractionMap OIL = CauldronInteraction.newInteractionMap("oil");
    public static final CauldronInteraction.InteractionMap FUEL = CauldronInteraction.newInteractionMap("fuel");

    public static void registerCauldronInteractions() {
        var oilMap = OIL.map();

        oilMap.put(GalacticraftItems.OIL_BUCKET.get(), GalacticraftCauldronInteraction::fillOilInteraction);
        oilMap.put(
                Items.BUCKET,
                (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteraction.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.OIL_BUCKET.toStack(), state -> state.getValue(FlammableCauldronBlock.FULL), SoundEvents.BUCKET_FILL_LAVA)
        );

        var fuelMap = FUEL.map();
        fuelMap.put(GalacticraftItems.FUEL_BUCKET.get(), GalacticraftCauldronInteraction::fillFuelInteraction);
        fuelMap.put(
                Items.BUCKET,
                (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteraction.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.FUEL_BUCKET.toStack(), state -> state.getValue(FlammableCauldronBlock.FULL), SoundEvents.BUCKET_FILL)
        );
    }

    private static InteractionResult fillOilInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteraction.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.OIL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
    }

    private static InteractionResult fillFuelInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteraction.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.FUEL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    }

    private static boolean isUnderWater(Level level, BlockPos pos) {
        FluidState fluidstate = level.getFluidState(pos.above());
        return fluidstate.is(FluidTags.WATER);
    }
}
