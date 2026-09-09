/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.world.level.block.AbstractPadBlock;
import io.kalishak.galacticraftlegacy.world.level.block.PadState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class VehicleItem extends Item {
    protected final Supplier<EntityType<?>> entityTypeSupplier;
    protected final Holder<Block> placedOn;

    public VehicleItem(Supplier<EntityType<?>> entityTypeSupplier, Holder<Block> placedOn, Item.Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
        this.placedOn = placedOn;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos blockPos = pos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack itemInHand = context.getItemInHand();

        if (player != null && !this.mayPlace(level, player, clickedFace, itemInHand, blockPos)) {
            return InteractionResult.FAIL;
        }

        Entity entity = this.entityTypeSupplier.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);

        if (entity instanceof VehicleEntity vehicleEntity) {
            EntityType.createDefaultStackConfig(level, itemInHand, player).apply(vehicleEntity);
            vehicleEntity.setPos(pos.getX(), pos.getY(), pos.getZ());

            if (!level.isClientSide()) {
                level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
                level.addFreshEntity(vehicleEntity);
            }

            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    protected boolean mayPlace(Level level, Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);

        if (state.is(this.placedOn)) {
            if (state.hasProperty(AbstractPadBlock.PAD_STATE)) {
                PadState padState = state.getValue(AbstractPadBlock.PAD_STATE);

                if (padState != PadState.CENTER) {
                    return false;
                }
            }

            return player.mayUseItemAt(blockPos, direction, itemStack);
        }

        return false;
    }
}
