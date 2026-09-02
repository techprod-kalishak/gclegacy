/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.advancement;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

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
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.ADVANCED_WAFER,
                        GalacticraftComponents.ADVANCEMENT_GC.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_GC_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_FABRICATED.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_FABRICATED_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_WAFERS.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_WAFERS_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED_DESC.asComponent(),
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
