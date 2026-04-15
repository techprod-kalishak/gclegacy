/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface RandomizableInventory extends ResourcefulContainer {
    String LOOT_TABLE_TAG = "LootTable";
    String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    @Nullable ResourceKey<LootTable> getLootTable();

    void setLootTable(@Nullable ResourceKey<LootTable> lootTable);

    long getLootTableSeed();

    void setLootTableSeed(long seed);

    default void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
        setLootTable(lootTable);
        setLootTableSeed(seed);
    }

    BlockPos getBlockPos();

    @Nullable Level getLevel();

    @Override
    default boolean isEmpty() {
        unpackLootTable(null);
        return ResourcefulContainer.super.isEmpty();
    }

    @Override
    default ItemStack getItem(int slot) {
        unpackLootTable(null);
        return ResourcefulContainer.super.getItem(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int amount) {
        unpackLootTable(null);
        return ResourcefulContainer.super.removeItem(slot, amount);
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        unpackLootTable(null);
        ResourcefulContainer.super.setItem(slot, stack);
    }

    static void setBlockEntityLootTable(BlockGetter level, RandomSource random, BlockPos blockPos, ResourceKey<LootTable> lootTable) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof RandomizableInventory randomizableInventory) {
            randomizableInventory.setLootTable(lootTable, random.nextLong());
        }
    }

    default boolean tryLoadLootTable(ValueInput input) {
        ResourceKey<LootTable> resourcekey = input.read("LootTable", LootTable.KEY_CODEC).orElse(null);
        setLootTable(resourcekey);
        setLootTableSeed(input.getLongOr("LootTableSeed", 0L));
        return resourcekey != null;
    }

    default boolean trySaveLootTable(ValueOutput output) {
        ResourceKey<LootTable> resourcekey = getLootTable();
        if (resourcekey == null) {
            return false;
        } else {
            output.store("LootTable", LootTable.KEY_CODEC, resourcekey);
            long i = getLootTableSeed();
            if (i != 0L) {
                output.putLong("LootTableSeed", i);
            }

            return true;
        }
    }

    default void unpackLootTable(@Nullable Player player) {
        Level level = getLevel();
        BlockPos blockpos = getBlockPos();
        ResourceKey<LootTable> resourcekey = getLootTable();

        if (resourcekey != null && level != null && level.getServer() != null) {
            LootTable loottable = level.getServer().reloadableRegistries().getLootTable(resourcekey);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, resourcekey);
            }

            setLootTable(null);
            LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos));
            if (player != null) {
                lootparams$builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }

            loottable.fill(this, lootparams$builder.create(LootContextParamSets.CHEST), getLootTableSeed());
        }
    }
}
