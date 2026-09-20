package io.kalishak.galacticraftlegacy.data.advancement;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.ClientAsset;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;

public class GalacticraftAdvancements extends AdvancementSubProvider {
    GalacticraftAdvancements(BootstrapContext<Advancement> output) {
        super(output);
    }

    @Override
    public void generate() {
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        new DisplayInfo(
                                new ItemStackTemplate(GalacticraftItems.ADVANCED_WAFER),
                                GalacticraftComponents.ADVANCEMENT_GC.asComponent(),
                                GalacticraftComponents.ADVANCEMENT_GC_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_COAL_POWER_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_FABRICATED.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_FABRICATED_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_WAFERS.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_WAFERS_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_GOLDEN_WAFERS_DESC.asComponent(),
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
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED.asComponent(),
                        GalacticraftComponents.ADVANCEMENT_COMPRESSED_DESC.asComponent(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItems(GalacticraftItems.COMPRESSOR))
                .parent(wafers)
                .save(this.output, Constants.id("compressed"));
    }
}
