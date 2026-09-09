/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface GearRenderState {
    ContextKey<ItemStack> THERMAL_CAP = new ContextKey<>(Constants.id("thermal_cap"));
    ContextKey<ItemStack> THERMAL_SHIRT = new ContextKey<>(Constants.id("thermal_shirt"));
    ContextKey<ItemStack> THERMAL_LEGGINGS = new ContextKey<>(Constants.id("thermal_leggings"));
    ContextKey<ItemStack> THERMAL_SOCKS = new ContextKey<>(Constants.id("thermal_socks"));
    ContextKey<ItemStack> OXYGEN_MASK = new ContextKey<>(Constants.id("oxygen_mask"));
    ContextKey<ItemStack> OXYGEN_GEAR = new ContextKey<>(Constants.id("oxygen_gear"));
    ContextKey<ItemStack> TANK = new ContextKey<>(Constants.id("left_tank"));
    ContextKey<ItemStack> ADDITIONAL_TANK = new ContextKey<>(Constants.id("right_tank"));
    ContextKey<ItemStack> PARACHUTE = new ContextKey<>(Constants.id("parachute"));
    ContextKey<Boolean> IS_PARACHUTE_OPEN = new ContextKey<>(Constants.id("is_parachute_open"));
    ContextKey<ItemStack> TELEMETRY_MODULE = new ContextKey<>(Constants.id("telemetry_module"));
    ContextKey<ItemStack> SHIELD_CONTROLLER = new ContextKey<>(Constants.id("shield_controller"));

    ContextKey<Float> TEMPERATURE_MODIFIER = new ContextKey<>(Constants.id("temperature_modifier"));

    static <E extends LivingEntity, S extends LivingEntityRenderState> void appendCommonRenderStates(E entity, S reusedState) {
        GearInventoryProvider gear = AttachmentHelper.getGearInventory(entity);
        SpaceGearEquipment spaceGearEquipment = gear.getGearEquipment();

        reusedState.setRenderData(GearRenderState.OXYGEN_MASK, spaceGearEquipment.get(GearEquipmentSlot.MASK));
        reusedState.setRenderData(GearRenderState.OXYGEN_GEAR, spaceGearEquipment.get(GearEquipmentSlot.GEAR));
        reusedState.setRenderData(GearRenderState.TANK, spaceGearEquipment.get(GearEquipmentSlot.TANK));
        reusedState.setRenderData(GearRenderState.ADDITIONAL_TANK, spaceGearEquipment.get(GearEquipmentSlot.ADDITIONAL_TANK));
    }

    static <E extends LivingEntity, S extends LivingEntityRenderState> void appendPlayerRenderStates(E entity, S reusedState) {
        GearInventoryProvider gear = AttachmentHelper.getGearInventory(entity);
        SpaceGearEquipment spaceGearEquipment = gear.getGearEquipment();
        CelestialBodyInfo celestialBodyData = CelestialBodyInfo.getFromLevel(entity.level());
        float temperatureModifier = 1.0F;

        if (celestialBodyData != null) {
            temperatureModifier = celestialBodyData.atmosphereInfo().getTemperatureModifier();
        }

        reusedState.setRenderData(GearRenderState.THERMAL_CAP, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_CAP));
        reusedState.setRenderData(GearRenderState.THERMAL_SHIRT, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_SHIRT));
        reusedState.setRenderData(GearRenderState.THERMAL_LEGGINGS, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_LEGGINGS));
        reusedState.setRenderData(GearRenderState.THERMAL_SOCKS, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_SOCKS));
        appendCommonRenderStates(entity, reusedState);
        reusedState.setRenderData(GearRenderState.PARACHUTE, spaceGearEquipment.get(GearEquipmentSlot.PARACHUTE));
        reusedState.setRenderData(GearRenderState.IS_PARACHUTE_OPEN, gear.usesParachute());
        reusedState.setRenderData(GearRenderState.TELEMETRY_MODULE, spaceGearEquipment.get(GearEquipmentSlot.FREQUENCY_MODULE));
        reusedState.setRenderData(GearRenderState.SHIELD_CONTROLLER, spaceGearEquipment.get(GearEquipmentSlot.SHIELD));
        reusedState.setRenderData(GearRenderState.TEMPERATURE_MODIFIER, temperatureModifier);
    }

    ItemStack oxygenMask();
    ItemStack oxygenGear();
    ItemStack leftTank();
    ItemStack rightTank();

    default ItemStack parachute() {
        return ItemStack.EMPTY;
    }

    default ItemStack telemetryModule() {
        return ItemStack.EMPTY;
    }

    default ItemStack shieldController() {
        return ItemStack.EMPTY;
    }
}
