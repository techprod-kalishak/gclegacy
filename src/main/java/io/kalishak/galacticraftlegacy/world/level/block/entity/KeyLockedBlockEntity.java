/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;


public abstract class KeyLockedBlockEntity extends NamedBlockEntity implements RandomizableInventory {
    protected KeyLock keyLock = KeyLock.UNLOCKED;
    protected NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    protected final ItemStacksResourceHandler itemHandler = new ItemStacksResourceHandler(this.items);
    protected @Nullable ResourceKey<LootTable> lootTable;
    protected long lootTableSeed = 0L;

    protected KeyLockedBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        setKeyLock(KeyLock.preGenTier(FeatureTier.TIER_1));
    }

    public void setKeyLock(KeyLock keyLock) {
        this.keyLock = keyLock;
    }

    public KeyLock getKeyLock() {
        return this.keyLock;
    }

    @Override
    public ResourceHandler<ItemResource> getResourceHandler() {
        return this.itemHandler;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        KeyLock.fromTag(input, this::setKeyLock);

        if (tryLoadLootTable(input)) {
            this.itemHandler.deserialize(input);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.keyLock.addToTag(output);

        if (!trySaveLootTable(output)) {
            this.itemHandler.serialize(output);
        }
    }

    public boolean canUnlock(ItemStack itemStack) {
        return this.keyLock.canUnlock(itemStack, getLevel(), getBlockPos());
    }

    public boolean isLocked() {
        return this.keyLock.locked();
    }

    public void unlock() {
        this.keyLock = KeyLock.UNLOCKED;
    }

    protected abstract @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory);

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        if (!this.isLocked()) {
            unpackLootTable(player);
            return createMenu(containerId, inventory);
        }

        return null;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.keyLock = componentGetter.getOrDefault(GalacticraftDataComponents.KEY_LOCK, KeyLock.UNLOCKED);

        SeededContainerLoot seededContainerLoot = componentGetter.get(DataComponents.CONTAINER_LOOT);

        if (seededContainerLoot != null) {
            this.lootTable = seededContainerLoot.lootTable();
            this.lootTableSeed = seededContainerLoot.seed();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (isLocked()) {
            components.set(GalacticraftDataComponents.KEY_LOCK, getKeyLock());
        }

        if (this.lootTable != null) {
            components.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(KeyLock.TAG_KEY);
        output.discard(RandomizableContainer.LOOT_TABLE_TAG);
        output.discard(RandomizableContainer.LOOT_TABLE_SEED_TAG);
    }
}
