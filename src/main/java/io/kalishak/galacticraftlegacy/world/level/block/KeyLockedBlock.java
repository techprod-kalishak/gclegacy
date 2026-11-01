/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.KeyLockedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public abstract class KeyLockedBlock extends BaseEntityBlock {
    public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

    public KeyLockedBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(UNLOCKED, false));
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof KeyLockedBlockEntity keyLockedBlockEntity) {
                if (keyLockedBlockEntity.isLocked()) {
                    BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf(pos), player, keyLockedBlockEntity.getDisplayName());
                } else {
                    player.openMenu(keyLockedBlockEntity, pos);
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(ItemStack stack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        KeyLock keyLock = stack.get(GalacticraftDataComponents.KEY_LOCK);

        if (keyLock != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof KeyLockedBlockEntity keyLockedBlockEntity) {
                if (keyLockedBlockEntity.canUnlock(stack)) {
                    keyLockedBlockEntity.unlock();
                    level.setBlockAndUpdate(pos, state.setValue(UNLOCKED, true));
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    stack.shrink(1);
                    player.openMenu(keyLockedBlockEntity, pos);
                } else if (keyLockedBlockEntity.isLocked()) {
                    BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf(pos), player, keyLockedBlockEntity.getDisplayName());
                }
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public @NonNull ItemStack getCloneItemStack(@NonNull LevelReader level, @NonNull BlockPos pos, @NonNull BlockState state, boolean includeData, @NonNull Player player) {
        ItemStack returned = super.getCloneItemStack(level, pos, state, includeData, player);

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof KeyLockedBlockEntity keyLockedBlockEntity) {
            if (keyLockedBlockEntity.isLocked()) {
                returned.set(GalacticraftDataComponents.KEY_LOCK, keyLockedBlockEntity.getKeyLock());
            }

            if (keyLockedBlockEntity.getLootTable() != null) {
                returned.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(keyLockedBlockEntity.getLootTable(), keyLockedBlockEntity.getLootTableSeed()));
            }
        }

        return returned;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNLOCKED);
    }
}
