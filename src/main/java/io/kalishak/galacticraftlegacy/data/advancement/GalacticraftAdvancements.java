/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.advancement;

import io.kalishak.galacticraftlegacy.advancements.FoundSpaceBossTrigger;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.component.SchematicVariant;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.BuiltinSchematicVariants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensions;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ChangeDimensionTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class GalacticraftAdvancements extends AdvancementSubProvider {
    public GalacticraftAdvancements(BootstrapContext<Advancement> output) {
        super(output);
    }

    @Override
    public void generate() {
        HolderGetter<Item> items = this.output.lookup(Registries.ITEM);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        new DisplayInfo(
                                new ItemStackTemplate(GalacticraftItems.ADVANCED_WAFER),
                                GalacticraftComponents.ADVANCEMENT_GC.translated(),
                                GalacticraftComponents.ADVANCEMENT_GC_DESC.translated(),
                                Optional.of(new ClientAsset.ResourceTexture(Constants.id("gui/advancements/backgrounds/space"))),
                                AdvancementType.TASK,
                                false,
                                false,
                                false
                        )
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(this.output, Constants.id("root"));
        AdvancementHolder coalPower = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.COAL_GENERATOR.get(),
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER.translated(),
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_coal_generator", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.COAL_GENERATOR))
                .parent(root)
                .save(this.output, Constants.id("coal_power"));
        AdvancementHolder fabricated = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.CIRCUIT_FABRICATOR.get(),
                        GalacticraftComponents.ADVANCEMENT_FABRICATED.translated(),
                        GalacticraftComponents.ADVANCEMENT_FABRICATED_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_circuit_fabricator", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.CIRCUIT_FABRICATOR))
                .parent(coalPower)
                .save(this.output, Constants.id("fabricated"));
        AdvancementHolder wafers = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.BASIC_WAFER.get(),
                        GalacticraftComponents.ADVANCEMENT_WAFERS.translated(),
                        GalacticraftComponents.ADVANCEMENT_WAFERS_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_basic_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.BASIC_WAFER))
                .parent(fabricated)
                .save(this.output, Constants.id("wafers"));
        AdvancementHolder goldenWafers = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.ADVANCED_WAFER.get(),
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS.translated(),
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.ADVANCED_WAFER))
                .parent(wafers)
                .save(this.output, Constants.id("golden_wafers"));
        AdvancementHolder compressed = Advancement.Builder.advancement()
                .display(
                        GalacticraftItems.COMPRESSOR.get(),
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED.translated(),
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.COMPRESSOR))
                .parent(wafers)
                .save(this.output, Constants.id("compressed"));

        generateMoon(items, root);
    }

    private void generateMoon(HolderGetter<Item> items, AdvancementHolder parent) {
        AdvancementHolder reachMoon = Advancement.Builder.advancement()
                .display(
                        new ItemStackTemplate(GalacticraftItems.MOON_DIRT),
                        GalacticraftComponents.ADVANCEMENT_MOON.translated(),
                        GalacticraftComponents.ADVANCEMENT_MOON_DESC.translated(),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("traveled_to_moon", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(GalacticraftDimensions.MOON))
                .parent(parent)
                .save(this.output, Constants.id("moon"));
        AdvancementHolder findMoonBoss = Advancement.Builder.advancement()
                .display(
                        new ItemStackTemplate(GalacticraftItems.MOON_BRICKS),
                        GalacticraftComponents.ADVANCEMENT_MOON_BOSS.translated(),
                        GalacticraftComponents.ADVANCEMENT_MOON_BOSS_DESC.translated(),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("found_moon_boss", FoundSpaceBossTrigger.TriggerInstance.foundBoss(GalacticraftEntityTypes.SKELETON_BOSS))
                .parent(reachMoon)
                .save(this.output, Constants.id("moon_dungeon"));
        AdvancementHolder getMoonKey = Advancement.Builder.advancement()
                .display(
                        new ItemStackTemplate(GalacticraftItems.MOON_DUNGEON_KEY),
                        GalacticraftComponents.ADVANCEMENT_KEY_T1.translated(),
                        GalacticraftComponents.ADVANCEMENT_KEY_T1_DESC.translated(),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_key",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(items, GalacticraftItems.MOON_DUNGEON_KEY)
                                .withComponents(
                                        DataComponentMatchers.Builder.components()
                                                .exact(DataComponentExactPredicate.expect(GalacticraftDataComponents.KEY_LOCK.get(), KeyLock.preGenTier(FeatureTier.TIER_1)))
                                                .build()
                                )
                        )
                )
                .parent(findMoonBoss)
                .save(this.output, Constants.id("key_t1"));
        AdvancementHolder getBuggySchematic = Advancement.Builder.advancement()
                .display(
                        new ItemStackTemplate(GalacticraftItems.MOON_DUNGEON_KEY),
                        GalacticraftComponents.ADVANCEMENT_MOON_BUGGY_SCHEMATIC.translated(),
                        GalacticraftComponents.ADVANCEMENT_MOON_BUGGY_SCHEMATIC_DESC.translated(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_buggy_schematic",
                        InventoryChangeTrigger.TriggerInstance.hasItems(hasSchematic(items, this.output.lookup(GalacticraftRegistries.Keys.SCHEMATIC), GalacticraftItems.SCHEMATIC, BuiltinSchematicVariants.MOON_BUGGY))
                )
                .parent(getMoonKey)
                .save(this.output, Constants.id("moon_buggy_schematic"));
        Advancement.Builder.advancement()
                .display(
                        new ItemStackTemplate(GalacticraftItems.MOON_DUNGEON_KEY),
                        GalacticraftComponents.ADVANCEMENT_MOON_BUGGY.translated(),
                        GalacticraftComponents.ADVANCEMENT_MOON_BUGGY_DESC.translated(),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_buggy",
                        InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.BUGGY)
                )
                .parent(getBuggySchematic)
                .save(this.output, Constants.id("moon_buggy"));
    }

    private static ItemPredicate hasSchematic(HolderGetter<Item> items, HolderGetter<SchematicVariant> variants, ItemLike item, ResourceKey<SchematicVariant> schematic) {
        return ItemPredicate.Builder.item()
                .of(items, item)
                .withComponents(
                        DataComponentMatchers.Builder.components()
                                .exact(DataComponentExactPredicate.expect(GalacticraftDataComponents.SCHEMATIC.get(), variants.getOrThrow(schematic)))
                                .build()
                )
                .build();
    }
}
