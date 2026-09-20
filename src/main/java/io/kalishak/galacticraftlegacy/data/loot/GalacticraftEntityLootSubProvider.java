/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetItemFluidTankFunction;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.predicates.GalacticraftLootItemConditions;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.stream.Stream;

public class GalacticraftEntityLootSubProvider extends EntityLootSubProvider {
    protected GalacticraftEntityLootSubProvider(EntityLootSubProvider.Context output) {
        super(FeatureFlags.DEFAULT_FLAGS, output);
    }

    @Override
    public void generate() {
        HolderGetter<Enchantment> enchantments = this.output.lookup(Registries.ENCHANTMENT);
        add(
                GalacticraftEntityType.EVOLVED_SKELETON.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(Items.ARROW)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(Items.BONE)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))

                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.FLUID_PIPE.white())
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.MEDIUM_TANK)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                                        .apply(SetItemFluidTankFunction.setFluid(
                                                                GalacticraftFluids.OXYGEN, ContextIntProviders.between(0, 500)
                                                        ))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.015F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                        )
                                        .when(GalacticraftLootItemConditions.withAdventureMode())
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.35F, 0.05F))
                        )
        );
        add(
                GalacticraftEntityType.EVOLVED_ZOMBIE.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(Items.ROTTEN_FLESH)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(LootItem.lootTableItem(Items.CARROT))
                                        .add(LootItem.lootTableItem(GalacticraftItems.OXYGEN_MASK))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_CARROT))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_POTATO))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.025F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.MEDIUM_TANK)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                                        .apply(SetItemFluidTankFunction.setFluid(
                                                                GalacticraftFluids.OXYGEN, ContextIntProviders.between(0, 500)
                                                        ))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.015F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(Items.MELON_SEEDS)
                                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                        )
                                        .when(GalacticraftLootItemConditions.withAdventureMode())
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.35F, 0.05F))
                        )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return GalacticraftEntityType.asStream();
    }
}
