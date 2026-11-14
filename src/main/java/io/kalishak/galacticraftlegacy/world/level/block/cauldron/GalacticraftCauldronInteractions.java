/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import io.kalishak.galacticraftlegacy.references.Constants;
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

public class GalacticraftCauldronInteractions {
    public static final Identifier OIL_ID = Constants.id("oil");
    public static final Identifier FUEL_ID = Constants.id("fuel");
    public static final Identifier BACTERIAL_SLUDGE_ID = Constants.id("sludge");
    public static final Identifier SULFURIC_ACID_ID = Constants.id("acid");
    public static final CauldronInteraction.Dispatcher OIL = new CauldronInteraction.Dispatcher();
    public static final CauldronInteraction.Dispatcher FUEL = new CauldronInteraction.Dispatcher();
    public static final CauldronInteraction.Dispatcher BACTERIAL_SLUDGE = new CauldronInteraction.Dispatcher();
    public static final CauldronInteraction.Dispatcher SULFURIC_ACID = new CauldronInteraction.Dispatcher();

    public static void init(IEventBus bus) {
        bus.addListener(GalacticraftCauldronInteractions::registerCauldronInteractions);
        bus.addListener(GalacticraftCauldronInteractions::registerCauldronDispatchers);
    }

    private static void registerCauldronInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        event.register(OIL_ID, GalacticraftItems.OIL_BUCKET.get(), GalacticraftCauldronInteractions::fillOilInteraction);
        event.register(OIL_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.OIL_BUCKET.toStack(), _ -> true, SoundEvents.BUCKET_FILL_LAVA));
        event.register(FUEL_ID, GalacticraftItems.FUEL_BUCKET.get(), GalacticraftCauldronInteractions::fillFuelInteraction);
        event.register(FUEL_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.FUEL_BUCKET.toStack(), _ -> true, SoundEvents.BUCKET_FILL));

        event.register(BACTERIAL_SLUDGE_ID, GalacticraftItems.BACTERIAL_SLUDGE_BUCKET.get(), GalacticraftCauldronInteractions::fillBacterialSludgeInteraction);
        event.register(BACTERIAL_SLUDGE_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.BACTERIAL_SLUDGE_BUCKET.toStack(), _ -> true, SoundEvents.BUCKET_FILL));
        event.register(SULFURIC_ACID_ID, GalacticraftItems.SULFURIC_ACID_BUCKET.get(), GalacticraftCauldronInteractions::fillSulfuricAcidInteraction);
        event.register(SULFURIC_ACID_ID, Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteractions.fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, GalacticraftItems.SULFURIC_ACID_BUCKET.toStack(), state -> state.getValue(AcidCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
    }

    private static void registerCauldronDispatchers(RegisterCauldronInteractionEvent.Dispatcher event) {
        event.register(OIL_ID, OIL);
        event.register(FUEL_ID, FUEL);
        event.register(BACTERIAL_SLUDGE_ID, BACTERIAL_SLUDGE);
        event.register(SULFURIC_ACID_ID, SULFURIC_ACID);
    }

    private static InteractionResult fillOilInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.OIL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
    }

    private static InteractionResult fillFuelInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.FUEL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    }

    private static InteractionResult fillBacterialSludgeInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.BACTERIAL_SLUDGE_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    }

    private static InteractionResult fillSulfuricAcidInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack) {
        return isUnderWater(level, pos) ? InteractionResult.CONSUME : CauldronInteractions.emptyBucket(level, pos, player, hand, filledStack, GalacticraftBlocks.SULFURIC_ACID_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);
    }

    private static boolean isUnderWater(Level level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos.above());
        return fluidState.is(FluidTags.WATER);
    }
}
