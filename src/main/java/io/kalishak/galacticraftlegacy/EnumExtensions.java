/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class EnumExtensions {
    public static final EnumProxy<RecipeBookType> RECIPE_BOOK_TYPE_FABRICATING = new EnumProxy<>(RecipeBookType.class);
    public static final EnumProxy<RecipeBookType> RECIPE_BOOK_TYPE_COMPRESSING = new EnumProxy<>(RecipeBookType.class);

    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_PARACHUTE = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:parachute");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_TANK = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:tank");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_THERMAL_PADDING = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:thermal_padding");
    public static final EnumProxy<EquipmentClientInfo.LayerType> LAYER_TYPE_THERMAL_PADDING_LEGGINGS = new EnumProxy<>(EquipmentClientInfo.LayerType.class, "galacticraftlegacy:thermal_padding_leggings");
}
