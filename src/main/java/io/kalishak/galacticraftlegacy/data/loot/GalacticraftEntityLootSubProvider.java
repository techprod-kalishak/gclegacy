/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.config.condition.GalacticraftConditions;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.GalacticraftLootFunctions;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetItemFluidTankFunction;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.predicates.GalacticraftLootItemConditions;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;

import java.util.stream.Stream;

public class GalacticraftEntityLootSubProvider extends EntityLootSubProvider {
    protected GalacticraftEntityLootSubProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    public void generate() {
        add(
                GalacticraftEntityType.EVOLVED_SKELETON.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.ARROW)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.BONE)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.WHITE_PIPE)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.MEDIUM_TANK)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                                        .apply(SetItemFluidTankFunction.setFluid(
                                                                GalacticraftFluids.OXYGEN, UniformInt.of(0, 500)
                                                        ))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.015F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                        )
                                        .when(GalacticraftLootItemConditions.withAdventureMode())
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.35F, 0.05F))
                        )
        );
        add(
                GalacticraftEntityType.EVOLVED_ZOMBIE.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.ROTTEN_FLESH)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(Items.CARROT))
                                        .add(LootItem.lootTableItem(GalacticraftItems.OXYGEN_MASK))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_CARROT))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_POTATO))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(GalacticraftItems.MEDIUM_TANK)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                                        .apply(SetItemFluidTankFunction.setFluid(
                                                                GalacticraftFluids.OXYGEN, UniformInt.of(0, 500)
                                                        ))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.015F, 0.01F))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.MELON_SEEDS)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                        )
                                        .when(GalacticraftLootItemConditions.withAdventureMode())
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.35F, 0.05F))
                        )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return GalacticraftEntityType.asStream();
    }
}
