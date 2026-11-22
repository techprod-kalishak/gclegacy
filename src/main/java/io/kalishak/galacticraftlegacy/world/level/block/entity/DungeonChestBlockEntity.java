/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.AbstractSpaceBoss;
import io.kalishak.galacticraftlegacy.world.inventory.DungeonChestMenu;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Continuation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.blockscan.BlockMatcher;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class DungeonChestBlockEntity extends KeyLockedBlockEntity implements LidBlockEntity {
    private final ChestLidController chestLidController = new ChestLidController();
    private FeatureTier featureTier = FeatureTier.TIER_1;

    public DungeonChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GalacticraftBlockEntityTypes.DUNGEON_CHEST.get(), blockPos, blockState);
    }

    public DungeonChestBlockEntity(BlockPos blockPos, BlockState blockState, FeatureTier featureTier) {
        this(blockPos, blockState);
        this.featureTier = featureTier;
        setKeyLock(KeyLock.preGenTier(featureTier));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, GalacticraftBlockEntityTypes.DUNGEON_CHEST.get(), (entity, _) -> entity.items);
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState state, DungeonChestBlockEntity dungeonChest) {
        dungeonChest.chestLidController.tickLid();
    }

    public static DungeonChestBlockEntity findClosest(AbstractSpaceBoss entity, FeatureTier chestTier) {
        AtomicReference<DungeonChestBlockEntity> chest = new AtomicReference<>();

        BlockMatcher blockMatcher = entity.level().findBlocksIn(entity.getBoundingBox().inflate(64, 8, 64))
                .filterPosition((_, pos) -> entity.level().getBlockEntity(pos) != null);

        blockMatcher.forEachUntil((pos, _) -> {
            BlockEntity blockEntity = entity.level().getBlockEntity(pos);

            if (blockEntity instanceof DungeonChestBlockEntity dungeonChestBlockEntity && dungeonChestBlockEntity.getFeatureTier() == chestTier) {
                Galacticraft.LOGGER.debug("Found chest to generate boss loot in: {}", pos);

                chest.set(dungeonChestBlockEntity);
            }

            return Continuation.abortIf(chest.get() != null);
        });

        if (chest.get() != null) {
            return chest.get();
        }

        Galacticraft.LOGGER.debug("Could not find chest to generate boss loot in!");
        return null;
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
    public int getSize() {
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
        return GalacticraftComponents.BLOCK_DUNGEON_CHEST.translated();
    }
}
