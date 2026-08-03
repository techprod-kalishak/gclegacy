/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RandomizableStorage {
    String LOOT_TABLE_TAG = "LootTable";
    String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    @Nullable ResourceKey<LootTable> getLootTable();

    void setLootTable(final @Nullable ResourceKey<LootTable> lootTable);

    default void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
        this.setLootTable(lootTable);
        this.setLootTableSeed(seed);
    }

    long getLootTableSeed();

    void setLootTableSeed(final long lootTableSeed);

    BlockPos getBlockPos();

    @Nullable Level getLevel();

    void setItem(int slot, ItemStack stack);

    ItemStack getItem(int slot);

    int getSize();

    default boolean tryLoadLootTable(ValueInput base) {
        ResourceKey<LootTable> lootTable = base.read("LootTable", LootTable.KEY_CODEC).orElse(null);

        setLootTable(lootTable);
        setLootTableSeed(base.getLongOr("LootTableSeed", 0L));

        return lootTable != null;
    }

    default boolean trySaveLootTable(ValueOutput base) {
        ResourceKey<LootTable> lootTable = getLootTable();

        if (lootTable == null) {
            return false;
        }

        base.store("LootTable", LootTable.KEY_CODEC, lootTable);

        long lootTableSeed = getLootTableSeed();

        if (lootTableSeed != 0L) {
            base.putLong("LootTableSeed", lootTableSeed);
        }

        return true;
    }

    default void unpackLootTable(@Nullable Player player) {
        Level level = getLevel();
        BlockPos worldPosition = getBlockPos();
        ResourceKey<LootTable> lootTableKey = getLootTable();

        if (lootTableKey != null && level != null && level.getServer() != null) {
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);

            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, lootTableKey);
            }

            setLootTable(null);

            LootParams.Builder params = new LootParams.Builder((ServerLevel)level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition));

            if (player != null) {
                params.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }

            fill(lootTable, this, params.create(LootContextParamSets.CHEST), getLootTableSeed());
        }
    }

    private static void fill(LootTable lootTable, RandomizableStorage randomizableStorage, LootParams params, long optionalRandomSeed) {
        LootContext context = new LootContext.Builder(params).withOptionalRandomSeed(optionalRandomSeed).create(lootTable.randomSequence);
        ObjectArrayList<ItemStack> itemStacks = lootTable.getRandomItems(context);
        RandomSource random = context.getRandom();
        List<Integer> availableSlots = getAvailableSlots(randomizableStorage, random);
        lootTable.shuffleAndSplitItems(itemStacks, availableSlots.size(), random);

        for (ItemStack itemStack : itemStacks) {
            if (availableSlots.isEmpty()) {
                return;
            }

            if (itemStack.isEmpty()) {
                randomizableStorage.setItem(availableSlots.removeLast(), ItemStack.EMPTY);
            } else {
                randomizableStorage.setItem(availableSlots.removeLast(), itemStack);
            }
        }
    }

    private static List<Integer> getAvailableSlots(RandomizableStorage randomizableStorage, RandomSource random) {
        ObjectArrayList<Integer> slots = new ObjectArrayList<>();

        for (int i = 0; i < randomizableStorage.getSize(); i++) {
            if (randomizableStorage.getItem(i).isEmpty()) {
                slots.add(i);
            }
        }

        Util.shuffle(slots, random);
        return slots;
    }
}
