/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.attachment.level.CelestialBodyLevelData;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.common.damagesource.IScalingFunction;

import java.util.Optional;

public class EnumExtensions {
    public static final EnumProxy<RecipeBookType> RECIPE_BOOK_TYPE_FABRICATING = new EnumProxy<>(RecipeBookType.class);

    public static final EnumProxy<DamageScaling> DAMAGE_SCALING_BY_CELESTIAL_BODY = new EnumProxy<>(DamageScaling.class, "galacticraftlegacy:by_celestial_body", (IScalingFunction) (source, target, amount, difficulty) -> {
        GearInventoryProvider inventoryProvider = AttachmentHelper.getGearInventory(target);
        float totalAmount = 0;

        if (source.is(GalacticraftDamageTypes.SUN_RADIATION)) {
            int missingParts = 0;

            for (int i = 0; i < 4; i++) {
                if (inventoryProvider.getGearEquipment().getResource(i).isEmpty()) {
                    missingParts++;
                }
            }

            Optional<CelestialBodyLevelData> levelData = AttachmentHelper.getMap(target.level(), GalacticraftAttachments.CELESTIAL_BODY, Holder::value);

            if (levelData.isPresent()) {
                if (levelData.get().temperatureModifier() > inventoryProvider.getThermalArmorEffectiveness()) {
                    totalAmount += (1.0F - inventoryProvider.getThermalArmorEffectiveness()) * 0.3F;
                }
            }

            if  (missingParts == 0) {
                return 0.0F;
            }

            totalAmount += missingParts * 0.4F;
        } else if (source.is(GalacticraftDamageTypes.SUFFOCATION)) {
            if (!inventoryProvider.mayBreath(target)) {
                totalAmount += 0.2F * IScalingFunction.DEFAULT.scaleDamage(source, target, amount, difficulty);
            }
        }

        return amount + totalAmount;
    });

    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_PARACHUTE = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:parachute");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_TANK = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:tank");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_THERMAL_PADDING = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:thermal_padding");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_THERMAL_PADDING_LEGGINGS = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:thermal_padding_leggings");
}
