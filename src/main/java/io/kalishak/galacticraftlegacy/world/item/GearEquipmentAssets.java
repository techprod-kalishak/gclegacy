package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

import static io.kalishak.galacticraftlegacy.Galacticraft.key;

public final class GearEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> THIN_THERMAL = key(EquipmentAssets.ROOT_ID, "thin_thermal");
    public static final ResourceKey<EquipmentAsset> THICK_THERMAL = key(EquipmentAssets.ROOT_ID, "thick_thermal");
    public static final ResourceKey<EquipmentAsset> LIGHT_TANK = key(EquipmentAssets.ROOT_ID,  "light_tank");
    public static final ResourceKey<EquipmentAsset> MEDIUM_TANK = key(EquipmentAssets.ROOT_ID,  "medium_tank");
    public static final ResourceKey<EquipmentAsset> DENSE_TANK = key(EquipmentAssets.ROOT_ID,  "dense_tank");
    public static final Map<DyeColor, ResourceKey<EquipmentAsset>> PARACHUTES = Util.makeEnumMap(DyeColor.class, color -> key(EquipmentAssets.ROOT_ID, color.getSerializedName() + "_parachute"));
}
