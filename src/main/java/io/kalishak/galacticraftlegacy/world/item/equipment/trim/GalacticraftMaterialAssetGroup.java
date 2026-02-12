package io.kalishak.galacticraftlegacy.world.item.equipment.trim;

import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;

import java.util.Map;

public final class GalacticraftMaterialAssetGroup {
    public static final MaterialAssetGroup CHEESE = MaterialAssetGroup.create( "cheese");
    public static final MaterialAssetGroup STEEL = MaterialAssetGroup.create( "steel", Map.of(GearEquipmentAssets.STEEL, "steel_darker"));
    public static final MaterialAssetGroup DESH = MaterialAssetGroup.create( "desh", Map.of(GearEquipmentAssets.DESH, "desh_darker"));
    public static final MaterialAssetGroup TITANIUM = MaterialAssetGroup.create( "titanium", Map.of(GearEquipmentAssets.TITANIUM, "titanium_darker"));
    public static final MaterialAssetGroup LEAD = MaterialAssetGroup.create( "lead");
}
