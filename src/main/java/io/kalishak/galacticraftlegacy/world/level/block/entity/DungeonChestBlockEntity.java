/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.inventory.DungeonChestMenu;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class DungeonChestBlockEntity extends KeyLockedBlockEntity implements LidBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("galacticraftlegacy.container.dungeon_chest");
    private final ChestLidController chestLidController = new ChestLidController();
    private FeatureTier featureTier = FeatureTier.TIER_1;

    public DungeonChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GalacticraftBlockEntityType.DUNGEON_CHEST.get(), blockPos, blockState);
    }

    public DungeonChestBlockEntity(BlockPos blockPos, BlockState blockState, FeatureTier featureTier) {
        this(blockPos, blockState);
        this.featureTier = featureTier;
        setKeyLock(KeyLock.preGenTier(featureTier));
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState state, DungeonChestBlockEntity dungeonChest) {
        dungeonChest.chestLidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    public FeatureTier getFeatureTier() {
        return this.featureTier;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    @Override
    protected @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new DungeonChestMenu(containerId, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }
}
