package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;

public interface GearRenderState {
    ContextKey<ItemStack> THERMAL_CAP = new ContextKey<>(Constants.id("thermal_cap"));
    ContextKey<ItemStack> THERMAL_SHIRT = new ContextKey<>(Constants.id("thermal_shirt"));
    ContextKey<ItemStack> THERMAL_LEGGINGS = new ContextKey<>(Constants.id("thermal_leggings"));
    ContextKey<ItemStack> THERMAL_SOCKS = new ContextKey<>(Constants.id("thermal_socks"));
    ContextKey<Boolean> HAS_OXYGEN_MASK = new ContextKey<>(Constants.id("has_oxygen_mask"));
    ContextKey<Boolean> HAS_OXYGEN_GEAR = new ContextKey<>(Constants.id("has_oxygen_gear"));
    ContextKey<ResourceKey<EquipmentAsset>> LEFT_TANK = new ContextKey<>(Constants.id("left_tank"));
    ContextKey<ResourceKey<EquipmentAsset>> RIGHT_TANK = new ContextKey<>(Constants.id("right_tank"));
    ContextKey<ResourceKey<EquipmentAsset>> PARACHUTE = new ContextKey<>(Constants.id("parachute"));
    ContextKey<Boolean> IS_PARACHUTE_VISIBLE = new ContextKey<>(Constants.id("is_parachute_visible"));
    ContextKey<Boolean> HAS_TELEMETRY = new ContextKey<>(Constants.id("has_telemetry"));
    ContextKey<Boolean> HAS_SHIELD = new ContextKey<>(Constants.id("has_shield"));

    ContextKey<Float> TEMPERATURE_MODIFIER = new ContextKey<>(Constants.id("temperature_modifier"));

    static <E extends LivingEntity, S extends LivingEntityRenderState> void appendCommonRenderStates(E entity, S reusedState) {
        GearInventoryProvider gear = AttachmentHelper.getGearInventory(entity);
        SpaceGearEquipment spaceGearEquipment = gear.getGearEquipment();

        reusedState.setRenderData(GearRenderState.HAS_OXYGEN_MASK, !spaceGearEquipment.get(GearEquipmentSlot.MASK).isEmpty());
        reusedState.setRenderData(GearRenderState.HAS_OXYGEN_GEAR, !spaceGearEquipment.get(GearEquipmentSlot.GEAR).isEmpty());
        GearEquippable.extractAssetId(spaceGearEquipment.get(GearEquipmentSlot.TANK)).ifPresent(gearEquipped -> reusedState.setRenderData(GearRenderState.LEFT_TANK, gearEquipped));
        GearEquippable.extractAssetId(spaceGearEquipment.get(GearEquipmentSlot.ADDITIONAL_TANK)).ifPresent(gearEquipped -> reusedState.setRenderData(GearRenderState.RIGHT_TANK, gearEquipped));
    }

    static <E extends LivingEntity, S extends LivingEntityRenderState> void appendPlayerRenderStates(E entity, S reusedState) {
        GearInventoryProvider gear = AttachmentHelper.getGearInventory(entity);
        SpaceGearEquipment spaceGearEquipment = gear.getGearEquipment();

        reusedState.setRenderData(GearRenderState.THERMAL_CAP, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_CAP));
        reusedState.setRenderData(GearRenderState.THERMAL_SHIRT, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_SHIRT));
        reusedState.setRenderData(GearRenderState.THERMAL_LEGGINGS, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_LEGGINGS));
        reusedState.setRenderData(GearRenderState.THERMAL_SOCKS, spaceGearEquipment.get(GearEquipmentSlot.THERMAL_SOCKS));
        appendCommonRenderStates(entity, reusedState);
        GearEquippable.extractAssetId(spaceGearEquipment.get(GearEquipmentSlot.PARACHUTE)).ifPresent(gearEquipped -> reusedState.setRenderData(GearRenderState.PARACHUTE, gearEquipped));
        reusedState.setRenderData(GearRenderState.IS_PARACHUTE_VISIBLE, gear.usesParachute());
        reusedState.setRenderData(GearRenderState.HAS_TELEMETRY, !spaceGearEquipment.get(GearEquipmentSlot.TELEMETRY).isEmpty());
        reusedState.setRenderData(GearRenderState.HAS_SHIELD, !spaceGearEquipment.get(GearEquipmentSlot.SHIELD).isEmpty());

        AttachmentHelper.getMap(entity.level(), GalacticraftAttachments.CELESTIAL_BODY, levelData -> levelData.value().temperatureModifier())
                .ifPresent(temperatureModifier -> reusedState.setRenderData(GearRenderState.TEMPERATURE_MODIFIER, temperatureModifier));
    }
}
