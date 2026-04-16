/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.world.entity.FlagData;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.entity.Flag;
import io.kalishak.galacticraftlegacy.world.item.component.FlagItemData;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlagItem extends Item {
    public FlagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }

        Vec3 vec3 = player.getViewVector(1.0F);
        List<Entity> entitiesInRange = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0)).inflate(1.0), EntitySelector.CAN_BE_PICKED);

        if (!entitiesInRange.isEmpty()) {
            Vec3 playerEyePosition = player.getEyePosition();

            for (Entity entity : entitiesInRange) {
                AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (aabb.contains(playerEyePosition)) {
                    return InteractionResult.PASS;
                }
            }
        }

        if (hitresult.getType() == HitResult.Type.BLOCK) {
            Vec3 clickedPosition = hitresult.getLocation();
            FlagData flagData = player instanceof ServerPlayer ? SpaceRaceHooks.getPlayerFlag((ServerPlayer) player) : FlagData.DEFAULT;
            Flag flag = new Flag(level, flagData, clickedPosition.x, clickedPosition.y, clickedPosition.z, (float) player.getY());

            if (!flag.isAlive()) {
                return InteractionResult.FAIL;
            }

            if (!level.isClientSide()) {
                level.addFreshEntity(flag);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                itemstack.consume(1, player);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player player) {
        super.onCraftedBy(stack, player);

        if (player instanceof ServerPlayer serverPlayer) {
            SpaceRaceHooks.getSpaceRaceTeam(serverPlayer)
                    .ifPresent(spaceRaceTeam -> stack.set(
                            GalacticraftDataComponents.FLAG,
                            new FlagItemData(spaceRaceTeam.getFlagData(), spaceRaceTeam.getDisplayName()))
                    );
        }
    }
}
