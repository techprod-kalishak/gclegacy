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
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
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
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.LivingEntityEquipmentWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class GearInventoryProvider implements ParachuteFalling {
    protected final SpaceGearEquipment gearEquipment;
    protected int parachuteFallingTicks;

    protected GearInventoryProvider(SpaceGearEquipment gearEquipment) {
        this.gearEquipment = gearEquipment;
    }

    public abstract void dropAll(LivingEntity entity);

    public abstract float getThermalArmorEffectiveness();

    public void serverGearTick(ServerLevel serverLevel, LivingEntity gearOwner) {
        //Oxygen

        Holder<CelestialBodyLevelData> celestialBodyLevelData = serverLevel.getData(GalacticraftAttachments.CELESTIAL_BODY);

        if (!gearOwner.getType().is(EntityTypeTags.UNDEAD)) {
            if (!mayBreath(gearOwner) && !depleteOxygen(gearOwner, 1)) {
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

                ItemStack shieldItem = getStackBySlot(GearEquipmentSlot.SHIELD);

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

    }

    public SpaceGearEquipment getGearEquipment() {
        return this.gearEquipment;
    }

    public ItemStack getStackBySlot(GearEquipmentSlot slot) {
        return ItemUtil.getStack(getGearEquipment(), slot.getIndex());
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
        ItemStack tank = getStackBySlot(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getStackBySlot(GearEquipmentSlot.ADDITIONAL_TANK);

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
        ItemStack tank = getStackBySlot(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getStackBySlot(GearEquipmentSlot.ADDITIONAL_TANK);
        ItemStack mask = getStackBySlot(GearEquipmentSlot.MASK);
        ItemStack gear = getStackBySlot(GearEquipmentSlot.GEAR);

        return !mask.isEmpty() && !gear.isEmpty() && (!tank.isEmpty() || additionalTank.isEmpty());
    }

    public boolean mayBreath(LivingEntity livingEntity) {
        Level level = livingEntity.level();
        Vec3 headPos = livingEntity.getEyePosition();
        Vec3i blockPos = new Vec3i(
                Mth.floor(headPos.x),
                Mth.floor(headPos.y),
                Mth.floor(headPos.z)
        );
        BlockState state = level.getBlockState(new BlockPos(blockPos));

        if (state.is(GalacticraftTags.Blocks.BREATHABLE_AIR)) {
            return true;
        }

        return hasCompleteOxygenSetup() && getRemainingOxygen() > 0;
    }

    protected boolean depleteOxygen(LivingEntity notifier, int amount) {
        ItemStack tank = getStackBySlot(GearEquipmentSlot.TANK);
        ItemStack additionalTank = getStackBySlot(GearEquipmentSlot.ADDITIONAL_TANK);
        int extracted = 0;
        GearEquipmentSlot effectiveTank = null;

        if (tank.isEmpty() && additionalTank.isEmpty()) {
            return false;
        }

        try (Transaction tx = Transaction.open(null)) {
            if (!tank.isEmpty()) {
                ResourceHandler<FluidResource> tankHandler = tank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndex(getGearEquipment(), GearEquipmentSlot.TANK.getIndex()));

                if (tankHandler != null) {
                    extracted = tankHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), amount, tx);

                    if (extracted > 0) {
                        effectiveTank = GearEquipmentSlot.TANK;
                    }
                }
            }

            if (!additionalTank.isEmpty()) {
                ResourceHandler<FluidResource> additionalTankHandler = additionalTank.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndex(getGearEquipment(), GearEquipmentSlot.ADDITIONAL_TANK.getIndex()));

                if (additionalTankHandler != null) {
                    extracted = additionalTankHandler.extract(FluidResource.of(GalacticraftFluids.OXYGEN), amount, tx);

                    if (extracted > 0) {
                        effectiveTank = GearEquipmentSlot.ADDITIONAL_TANK;
                    }
                }
            }
        }

//        if (extracted > 0 && notifier instanceof ServerPlayer serverPlayer) {
//            PacketDistributor.sendToPlayer(serverPlayer, new UpdateStoredOxygenPayload(serverPlayer.getId(), getStackBySlot(effectiveTank), effectiveTank));
//        }

        return extracted > 0;
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

//                    if (slot.isTank() && entity instanceof ServerPlayer serverPlayer) {
//                        PacketDistributor.sendToPlayer(serverPlayer, new UpdateStoredOxygenPayload(serverPlayer.getId(), newStack, slot));
//                    }
                }
            }
        }
    }
}
