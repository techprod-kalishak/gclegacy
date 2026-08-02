/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.inventory.container.RandomizableStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class RandomizableStorageBlockEntity extends BaseItemStorageBlockEntity implements RandomizableStorage {
    protected @Nullable ResourceKey<LootTable> lootTable;
    protected long lootTableSeed = 0L;

    protected RandomizableStorageBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public boolean isEmpty() {
        unpackLootTable(null);
        return super.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        unpackLootTable(null);
        return super.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        unpackLootTable(null);
        return super.removeItem(slot, count);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        unpackLootTable(null);
        super.setItem(slot, stack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        if (canOpen(player)) {
            unpackLootTable(inventory.player);
            return createMenu(containerId, inventory);
        }

        if (!player.isSpectator()) {
            BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf(getBlockPos()), player, getDisplayName());
        }

        return null;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        SeededContainerLoot loot = components.get(DataComponents.CONTAINER_LOOT);

        if (loot != null) {
            this.lootTable = loot.lootTable();
            this.lootTableSeed = loot.seed();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (this.lootTable != null) {
            components.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("LootTable");
        output.discard("LootTableSeed");
    }
}
