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
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;


public abstract class KeyLockedBlockEntity extends RandomizableStorageBlockEntity {
    protected KeyLock keyLock = KeyLock.UNLOCKED;

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
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        KeyLock.fromTag(input, this::setKeyLock);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.keyLock.addToTag(output);
    }

    public boolean canUnlock(ItemStack itemStack) {
        return this.keyLock.canUnlock(itemStack, getLevel(), getBlockPos());
    }

    @Override
    public boolean canOpen(Player player) {
        return super.canOpen(player) && isLocked();
    }

    public boolean isLocked() {
        return this.keyLock.locked();
    }

    public void unlock() {
        this.keyLock = KeyLock.UNLOCKED;
    }

    protected abstract @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory);

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.keyLock = componentGetter.getOrDefault(GalacticraftDataComponents.KEY_LOCK, KeyLock.UNLOCKED);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (isLocked()) {
            components.set(GalacticraftDataComponents.KEY_LOCK, getKeyLock());
        }
    }
}
