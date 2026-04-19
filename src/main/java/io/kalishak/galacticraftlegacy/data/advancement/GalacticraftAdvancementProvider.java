/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.advancement;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GalacticraftAdvancementProvider {
    public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new AdvancementProvider(
                output,
                registries,
                List.of(
                        GalacticraftAdvancementProvider::buildRoot
                )
        );
    }

    private static void buildRoot(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.ADVANCED_WAFER,
                        Component.translatable("advancements.galacticraftlegacy.galacticraft.title"),
                        Component.translatable("advancements.galacticraftlegacy.galacticraft.description"),
                        Constants.id("gui/advancements/backgrounds/space"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(output, Constants.id("root"));
        AdvancementHolder coalPower = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.COAL_GENERATOR,
                        Component.translatable("advancements.galacticraftlegacy.coal_power.title"),
                        Component.translatable("advancements.galacticraftlegacy.coal_power.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_coal_generator", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.COAL_GENERATOR))
                .parent(root)
                .save(output, Constants.id("coal_power"));
        AdvancementHolder fabricated = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.CIRCUIT_FABRICATOR,
                        Component.translatable("advancements.galacticraftlegacy.fabricated.title"),
                        Component.translatable("advancements.galacticraftlegacy.fabricated.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_circuit_fabricator", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.CIRCUIT_FABRICATOR))
                .parent(coalPower)
                .save(output, Constants.id("fabricated"));
        AdvancementHolder wafers = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.BASIC_WAFER,
                        Component.translatable("advancements.galacticraftlegacy.wafers.title"),
                        Component.translatable("advancements.galacticraftlegacy.wafers.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_basic_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.BASIC_WAFER))
                .parent(fabricated)
                .save(output, Constants.id("wafers"));
        AdvancementHolder goldenWafers = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.ADVANCED_WAFER,
                        Component.translatable("advancements.galacticraftlegacy.golden_wafers.title"),
                        Component.translatable("advancements.galacticraftlegacy.golden_wafers.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.ADVANCED_WAFER))
                .parent(wafers)
                .save(output, Constants.id("golden_wafers"));
        AdvancementHolder compressed = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.COMPRESSOR,
                        Component.translatable("advancements.galacticraftlegacy.compressed.title"),
                        Component.translatable("advancements.galacticraftlegacy.compressed.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.COMPRESSOR))
                .parent(wafers)
                .save(output, Constants.id("compressed"));
    }
}
