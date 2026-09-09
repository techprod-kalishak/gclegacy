/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.loot;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetItemFluidTankFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record GalacticraftChestLootSubProvider(HolderLookup.Provider registries) implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        output.accept(
                GalacticraftLootTables.MOON_DUNGEON,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(GalacticraftItems.CHEESE_CHUNK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_APPLE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_CARROT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_MELON).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_POTATO).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.FLUID_CANISTER).setWeight(5).apply(SetItemFluidTankFunction.setFluid(GalacticraftFluids.OIL, ConstantInt.of(1000))))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
                                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                                        .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F)))
                                        .add(LootItem.lootTableItem(Items.NOTE_BLOCK).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_FAR).setWeight(4))
                                        .add(LootItem.lootTableItem(GalacticraftItems.STEEL_SHOVEL).setWeight(10).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.5F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.SAPPHIRE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.RAW_METEORIC_IRON).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.FREQUENCY_MODULE).setWeight(1))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                        )
        );
        output.accept(
                GalacticraftLootTables.MARS_DUNGEON,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        //.add(LootItem.lootTableItem(GalacticraftItems.CARBON_FRAGMENTS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_APPLE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_CARROT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_MELON).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_POTATO).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.FLUID_CANISTER).setWeight(5).apply(SetItemFluidTankFunction.setFluid(GalacticraftFluids.OIL, ConstantInt.of(1000))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_HELMET).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_CHESTPLATE).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_LEGGINGS).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_BOOTS).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F)))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_MALL).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_MELLOHI).setWeight(4))
                                        .add(LootItem.lootTableItem(GalacticraftItems.RAW_DESH).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        //.add(LootItem.lootTableItem(GalacticraftItems.DESH_STICK).setWeight(5))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment().withOptions(enchantments.getOrThrow(EnchantmentTags.TREASURE))))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment().withOptions(enchantments.getOrThrow(EnchantmentTags.TREASURE))))
                        )
        );
        output.accept(
                GalacticraftLootTables.VENUS_DUNGEON,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_APPLE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_CARROT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_MELON).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DEHYDRATED_POTATO).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.FLUID_CANISTER).setWeight(5).apply(SetItemFluidTankFunction.setFluid(GalacticraftFluids.OIL, ConstantInt.of(1000))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_HELMET).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_CHESTPLATE).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_LEGGINGS).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        .add(LootItem.lootTableItem(GalacticraftItems.DESH_BOOTS).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.1F))))
                                        //.add(LootItem.lootTableItem(GalacticraftItems.SULFURIC_ACID_BUCKET).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                                        .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F)))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_CHIRP).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_WAIT).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.MUSIC_DISC_WARD).setWeight(4))
                                        .add(LootItem.lootTableItem(GalacticraftItems.ISOTHERMAL_FABRIC).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment().withOptions(enchantments.getOrThrow(EnchantmentTags.TREASURE))))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment().withOptions(enchantments.getOrThrow(EnchantmentTags.TREASURE))))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment().withOptions(enchantments.getOrThrow(EnchantmentTags.TREASURE))))
                        )
        );
    }
}
