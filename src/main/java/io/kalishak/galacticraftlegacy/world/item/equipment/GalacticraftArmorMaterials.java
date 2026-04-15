/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.equipment;

import com.google.common.collect.Maps;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Map;

public class GalacticraftArmorMaterials {
    public static final  ArmorMaterial DESH = new ArmorMaterial(
            42, makeDefense(4, 7, 9, 4, 6), 8, SoundEvents.ARMOR_EQUIP_IRON, 2.0F, 3.0F, GalacticraftTags.Items.REPAIRS_DESH_ARMOR, GearEquipmentAssets.DESH
    );
    public static final  ArmorMaterial STEEL = new ArmorMaterial(
            30, makeDefense(3, 6, 8, 3, 5), 9, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 1.0F, GalacticraftTags.Items.REPAIRS_STEEL_ARMOR, GearEquipmentAssets.STEEL
    );
    public static final  ArmorMaterial TITANIUM = new ArmorMaterial(
            26, makeDefense(5, 7, 10, 5, 7), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 1.0F, GalacticraftTags.Items.REPAIRS_TITANIUM_ARMOR, GearEquipmentAssets.TITANIUM
    );

    static Map<ArmorType, Integer> makeDefense(int boots, int leggings, int chestplate, int helmet, int body) {
        return Maps.newEnumMap(
                Map.of(
                        ArmorType.BOOTS,
                        boots,
                        ArmorType.LEGGINGS,
                        leggings,
                        ArmorType.CHESTPLATE,
                        chestplate,
                        ArmorType.HELMET,
                        helmet,
                        ArmorType.BODY,
                        body
                )
        );
    }
}
