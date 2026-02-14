package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

import static io.kalishak.galacticraftlegacy.Constants.key;

public final class GearEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> DESH = key(EquipmentAssets.ROOT_ID, "desh");
    public static final ResourceKey<EquipmentAsset> SENSOR_GLASSES = key(EquipmentAssets.ROOT_ID, "sensor_glasses");
    public static final ResourceKey<EquipmentAsset> STEEL = key(EquipmentAssets.ROOT_ID, "steel");
    public static final ResourceKey<EquipmentAsset> TITANIUM = key(EquipmentAssets.ROOT_ID, "titanium");

    public static final ResourceKey<EquipmentAsset> THERMAL = key(EquipmentAssets.ROOT_ID, "thermal_padding");
    public static final ResourceKey<EquipmentAsset> ISOTHERMAL = key(EquipmentAssets.ROOT_ID, "isothermal_padding");
    public static final ResourceKey<EquipmentAsset> WOLF_THERMAL = key(EquipmentAssets.ROOT_ID, "wolf_thermal_padding");
    public static final ResourceKey<EquipmentAsset> LIGHT_TANK = key(EquipmentAssets.ROOT_ID,  "light_oxygen_tank");
    public static final ResourceKey<EquipmentAsset> MEDIUM_TANK = key(EquipmentAssets.ROOT_ID,  "medium_oxygen_tank");
    public static final ResourceKey<EquipmentAsset> HEAVY_TANK = key(EquipmentAssets.ROOT_ID,  "heavy_oxygen_tank");
    public static final Map<DyeColor, ResourceKey<EquipmentAsset>> PARACHUTES = Util.makeEnumMap(DyeColor.class, color -> key(EquipmentAssets.ROOT_ID, color.getSerializedName() + "_parachute"));
}
