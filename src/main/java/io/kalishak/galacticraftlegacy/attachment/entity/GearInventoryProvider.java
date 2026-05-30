/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import io.kalishak.galacticraftlegacy.world.item.component.ShieldController;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.LivingEntityEquipmentWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class GearInventoryProvider implements ParachuteFalling {
    protected final SpaceGearEquipment gearEquipment;
    protected int parachuteFallingTicks;

    protected GearInventoryProvider(SpaceGearEquipment gearEquipment) {
        this.gearEquipment = gearEquipment;
    }

    public abstract void dropAll(ServerLevel level, @NonNull LivingEntity gearOwner, @Nullable DamageSource cause);

    public abstract float getThermalArmorEffectiveness();

    public void serverGearTick(ServerLevel serverLevel, LivingEntity gearOwner) {
        //Oxygen
        this.gearEquipment.tick(gearOwner);
        SpaceGearEquipment spaceGearEquipment = getGearEquipment();
        Holder<CelestialBodyLevelData> celestialBodyLevelData = serverLevel.getData(GalacticraftAttachments.CELESTIAL_BODY);

        if (!gearOwner.is(EntityTypeTags.UNDEAD)) {
            if (!mayBreath(gearOwner) && !depleteOxygen()) {
                gearOwner.hurtServer(serverLevel, serverLevel.damageSources().source(GalacticraftDamageTypes.SUFFOCATION), 2.0F);
            }

            if (celestialBodyLevelData.value().temperatureModifier() != 0) {
                float effectiveness = getThermalArmorEffectiveness();

                if (effectiveness < 1.0F) {
                    gearOwner.hurtServer(serverLevel, serverLevel.damageSources().source(GalacticraftDamageTypes.SUN_RADIATION), 2.0F * (1.0F - effectiveness));
                }
            }

            if (celestialBodyLevelData.value().atmosphereInfo().isCorrosive()) {
                boolean isProtected = false;

                ItemStack shieldItem = spaceGearEquipment.get(GearEquipmentSlot.SHIELD);

                if (!shieldItem.isEmpty()) {
                    isProtected = true;

                    ShieldController shieldController = shieldItem.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

                    if (shieldController != null && shieldController.getTicksBeforeFatalDamage() > 0) {
                        shieldController.depleteByValue(shieldItem, gearOwner, serverLevel, 1);

                        if (shieldController.getTicksBeforeFatalDamage() == 0) {
                            isProtected = false;
                        }
                    }
                }

                if (!isProtected) {
                    ResourceHandler<ItemResource> armorSlots = LivingEntityEquipmentWrapper.of(gearOwner, EquipmentSlot.Type.HUMANOID_ARMOR);

                    for (int i = 0; i < armorSlots.size(); i++) {
                        ItemResource itemResource = armorSlots.getResource(i);

                        if (!itemResource.isEmpty()) {
                            Equippable equippable = itemResource.get(DataComponents.EQUIPPABLE);

                            if (equippable != null && equippable.damageOnHurt()) {
                                itemResource.toStack().hurtAndBreak(1, serverLevel, gearOwner, item -> {});
                            }
                        }
                    }
                }
            }
        }
    }

    public void clientGearTick(Level level, LivingEntity gearOwner) {
        this.gearEquipment.tick(gearOwner);
    }

    public SpaceGearEquipment getGearEquipment() {
        return this.gearEquipment;
    }

    @Override
    public void onLand(Entity owner, BlockPos landedPos) {
        if (!owner.onGround()) {
            setFallingTicks(getFallingTicks() + 1);
            //gearOwner.setPose(Pose.STANDING);
        }
    }

    @Override
    public void setFallingTicks(int parachuteFallingTicks) {
        this.parachuteFallingTicks = parachuteFallingTicks;
    }

    @Override
    public int getFallingTicks() {
        return this.parachuteFallingTicks;
    }

    public int getRemainingOxygen() {
        ItemStack tank = getGearEquipment().get(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getGearEquipment().get(GearEquipmentSlot.ADDITIONAL_TANK);

        if (tank.isEmpty() && additionalTank.isEmpty()) {
            return 0;
        }

        int oxygenRemaining = 0;

        ResourceHandler<FluidResource> tankHandler = tank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndexStrict(getGearEquipment(), GearEquipmentSlot.TANK.getIndex()));

        if (tankHandler != null) {
            oxygenRemaining += tankHandler.getAmountAsInt(0);
        }

        ResourceHandler<FluidResource> additionalTankHandler = additionalTank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndexStrict(getGearEquipment(), GearEquipmentSlot.ADDITIONAL_TANK.getIndex()));

        if (additionalTankHandler != null) {
            oxygenRemaining += additionalTankHandler.getAmountAsInt(0);
        }

        return oxygenRemaining;
    }

    public boolean hasCompleteOxygenSetup() {
        ItemStack tank = getGearEquipment().get(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getGearEquipment().get(GearEquipmentSlot.ADDITIONAL_TANK);
        ItemStack mask = getGearEquipment().get(GearEquipmentSlot.MASK);
        ItemStack gear = getGearEquipment().get(GearEquipmentSlot.GEAR);

        return !mask.isEmpty() && !gear.isEmpty() && (!tank.isEmpty() || !additionalTank.isEmpty());
    }

    public boolean mayBreath(LivingEntity livingEntity) {
        if (livingEntity.is(EntityTypeTags.UNDEAD)) {
            return true;
        }

        Level level = livingEntity.level();
        Vec3 headPos = livingEntity.getEyePosition();
        BlockPos blockPos = new BlockPos(
                Mth.floor(headPos.x),
                Mth.floor(headPos.y),
                Mth.floor(headPos.z)
        );
        BlockState state = level.getBlockState(blockPos);

        if (state.is(GalacticraftTags.Blocks.BREATHABLE_AIR)) {
            return true;
        }

        return hasCompleteOxygenSetup() && getRemainingOxygen() > 0;
    }

    protected boolean depleteOxygen() {
        ItemStack tank = getGearEquipment().get(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getGearEquipment().get(GearEquipmentSlot.ADDITIONAL_TANK);

        if (tank.isEmpty() && additionalTank.isEmpty()) {
            return false;
        }

        try (Transaction tx = Transaction.open(null)) {
            if (!tank.isEmpty()) {
                ResourceHandler<FluidResource> tankHandler = tank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndex(getGearEquipment(), GearEquipmentSlot.TANK.getIndex()));

                if (tankHandler != null) {
                    if (tankHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), 1, tx) > 0) {
                        tx.commit();
                        return true;
                    }
                }
            }

            if (!additionalTank.isEmpty()) {
                ResourceHandler<FluidResource> additionalTankHandler = additionalTank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndex(getGearEquipment(), GearEquipmentSlot.ADDITIONAL_TANK.getIndex()));

                if (additionalTankHandler != null) {
                    if (additionalTankHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), 1, tx) > 0) {
                        tx.commit();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void onGearEquipped(LivingEntity entity, GearEquipmentSlot slot, ItemStack newStack, ItemStack oldStack) {
        if (!entity.level().isClientSide() && !entity.isSpectator()) {
            if (!ItemStack.isSameItemSameComponents(oldStack, newStack)) {
                GearEquippable gearEquippable = newStack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

                if (gearEquippable != null && slot == gearEquippable.gearSlot()) {
                    if (!entity.isSilent()) {
                        entity.level().playSeededSound(
                                null,
                                entity.getX(),
                                entity.getY(),
                                entity.getZ(),
                                gearEquippable.equipSound(),
                                entity.getSoundSource(),
                                1.0F,
                                1.0F,
                                entity.getRandom().nextLong()
                        );
                    }
                }
            }
        }
    }
}
