/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.resources.Identifier;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;

public class GalacticraftCauldronInteraction {
    public static final Identifier OIL_ID = Constants.id("oil");
    public static final Identifier FUEL_ID = Constants.id("fuel");
    public static final CauldronInteraction.Dispatcher OIL = new CauldronInteraction.Dispatcher();
    public static final CauldronInteraction.Dispatcher FUEL = new CauldronInteraction.Dispatcher();

    public static void init(IEventBus bus) {
        bus.addListener(GalacticraftCauldronInteraction::registerCauldronDispatchers);
        bus.addListener(GalacticraftCauldronInteraction::registerCauldronInteractions);
    }

    private static void registerCauldronDispatchers(RegisterCauldronInteractionEvent.Dispatcher event) {
        event.register(OIL_ID, OIL);
        event.register(FUEL_ID, FUEL);
    }

    private static void registerCauldronInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        event.register(OIL_ID, GalacticraftItems.OIL_BUCKET.get(), GalacticraftCauldronInteraction::fillOilInteraction);
        event.register(OIL_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.OIL_BUCKET.toStack(), state -> state.getValue(FlammableCauldronBlock.FULL), SoundEvents.BUCKET_FILL_LAVA));
        event.register(FUEL_ID, GalacticraftItems.FUEL_BUCKET.get(), GalacticraftCauldronInteraction::fillFuelInteraction);
        event.register(FUEL_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.FUEL_BUCKET.toStack(), state -> state.getValue(FlammableCauldronBlock.FULL), SoundEvents.BUCKET_FILL));
    }

    private static InteractionResult fillOilInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.OIL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
    }

    private static InteractionResult fillFuelInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.FUEL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    }

    private static boolean isUnderWater(Level level, BlockPos pos) {
        FluidState fluidstate = level.getFluidState(pos.above());
        return fluidstate.is(FluidTags.WATER);
    }
}
