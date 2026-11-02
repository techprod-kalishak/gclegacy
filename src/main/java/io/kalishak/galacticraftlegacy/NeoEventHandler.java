/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.data.datamap.Extinguishable;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.transfer.node.NodeNetwork;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlotGroup;
import io.kalishak.galacticraftlegacy.world.item.component.GearAttributeModifiers;
import io.kalishak.galacticraftlegacy.world.item.component.ShieldController;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class NeoEventHandler {
    NeoEventHandler() {}

    @SubscribeEvent
    public void updateRecipes(OnDatapackSyncEvent event) {
        event.sendRecipes(GalacticraftRecipeType.CIRCUIT.get());
    }

    @SubscribeEvent
    public void appendHoverText(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();

        GearAttributeModifiers attributeModifiers = stack.get(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS);

        if (attributeModifiers != null) {
            if (event.getContext().tooltipDisplay().shows(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS.get())) {
                for (GearEquipmentSlotGroup slotGroup : GearEquipmentSlotGroup.values()) {
                    MutableBoolean first =  new MutableBoolean(true);

                    attributeModifiers.forEach(slotGroup, (attribute, modifier, tooltip) -> {
                        if (tooltip != GearAttributeModifiers.Display.hidden()) {
                            if (first.isTrue()) {
                                event.addTooltipLines(CommonComponents.EMPTY, Component.translatable("item.galacticraftlegacy.modifiers." + slotGroup.getSerializedName()).withStyle(ChatFormatting.GRAY));
                                first.setFalse();
                            }

                            tooltip.apply(event::addTooltipLines, event.getContext().player(), attribute, modifier);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        DamageSource damageSource = event.getSource();

        if (victim.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
            if (AttachmentHelper.hasGearInventory(victim)) {
                GearInventoryProvider provider = AttachmentHelper.getGearInventory(victim);

                if (victim.level() instanceof ServerLevel serverLevel) {
                    GameRules gameRules = serverLevel.getGameRules();

                    if ((victim instanceof Player && !gameRules.get(GameRules.KEEP_INVENTORY)) || gameRules.get(GameRules.ENTITY_DROPS)) {
                        provider.dropAll(serverLevel, victim, damageSource);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void entityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity livingEntity && entity.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR) && AttachmentHelper.hasGearInventory(entity)) {
            GearInventoryProvider gearInventoryProvider = AttachmentHelper.getGearInventory(entity);

            if (entity.level() instanceof ServerLevel serverLevel) {
                gearInventoryProvider.serverGearTick(serverLevel, livingEntity);
            } else {
                gearInventoryProvider.clientGearTick(livingEntity);
            }
        }
    }

    @SubscribeEvent
    public void handleEntityDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        boolean bypasses = source.is(GalacticraftTags.DamageTypes.BYPASSES_SHIELD_CONTROLLER);

        if (entity.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR) && AttachmentHelper.hasGearInventory(entity)) {
            ItemStack shieldItem = AttachmentHelper.getGearInventory(entity).getGearEquipment().get(GearEquipmentSlot.SHIELD);

            if (!shieldItem.isEmpty() && entity.level() instanceof ServerLevel serverLevel) {
                if (!bypasses && !entity.isInvulnerableTo(serverLevel, source)) {
                    event.setAmount(0.0F);

                    ShieldController shieldController = shieldItem.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

                    if (shieldController != null) {
                        shieldController.depleteByValue(shieldItem, entity, serverLevel, (int) Math.floor(event.getOriginalAmount()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (AttachmentHelper.hasGearInventory(player)) {
            GearInventoryProvider gearInventory = AttachmentHelper.getGearInventory(player);

            if (player.level() instanceof ServerLevel serverLevel) {
                gearInventory.serverGearTick(serverLevel, player);
            } else {
                gearInventory.clientGearTick(player);
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor level = event.getLevel();

        if (!level.isClientSide() && level instanceof Level) {
            BlockPos placementPos = event.getPos();
            BlockState newState = event.getPlacedBlock();
            Holder<Block> blockToPlace = newState.typeHolder();
            Extinguishable extinguishable = BuiltInRegistries.BLOCK.getData(GalacticraftDataMaps.EXTINGUISHED_WITHOUT_OXYGEN, blockToPlace.unwrapKey().orElseThrow());

            if (extinguishable != null || newState.is(Blocks.CAMPFIRE)) {
                if (!OxygenHelper.hasOxygenNearby((Level) level, placementPos, 1.0D, false)) {
                    BlockState unlitState = null;

                    if (newState.is(Blocks.CAMPFIRE)) {
                        unlitState = Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false);
                    } else if (extinguishable != null) {
                        unlitState = extinguishable.unlitState();

                        if (extinguishable.hasFacingProperty()) {
                            unlitState = unlitState.setValue(BlockStateProperties.HORIZONTAL_FACING, newState.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        }
                    }

                    if (unlitState != null) {
                        level.playSound(null, placementPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.setBlock(placementPos, unlitState, Block.UPDATE_ALL);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerLoad(ServerStartingEvent event) {
        SpaceRaceHooks.onServerLoad(event);
        NodeNetwork.onServerLoad(event);
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        NodeNetwork.levelTick(event);

//        if (event.getLevel() instanceof ServerLevel serverLevel) {
//            FallenMeteor.createFallingMeteor(serverLevel);
//        }
    }

    @SubscribeEvent
    public void onServerStop(ServerStoppingEvent event) {
        SpaceRaceHooks.onServerStop(event);
        NodeNetwork.onServerStop(event);
    }
}
