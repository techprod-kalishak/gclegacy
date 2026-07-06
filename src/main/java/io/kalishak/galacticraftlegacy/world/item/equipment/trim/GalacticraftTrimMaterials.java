/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.equipment.trim;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.function.UnaryOperator;

public final class GalacticraftTrimMaterials {
    public static final ResourceKey<TrimMaterial> STEEL = Constants.key(Registries.TRIM_MATERIAL, "steel");
    public static final ResourceKey<TrimMaterial> CHEESE = Constants.key(Registries.TRIM_MATERIAL, "cheese");
    public static final ResourceKey<TrimMaterial> DESH = Constants.key(Registries.TRIM_MATERIAL, "desh");
    public static final ResourceKey<TrimMaterial> TITANIUM = Constants.key(Registries.TRIM_MATERIAL, "titanium");
    public static final ResourceKey<TrimMaterial> LEAD = Constants.key(Registries.TRIM_MATERIAL, "lead");

    public static void bootstrap(BootstrapContext<TrimMaterial> cxt) {
        register(cxt, STEEL, style -> style.withColor(5263440), GalacticraftMaterialAssetGroup.STEEL);
        register(cxt, CHEESE, style -> style.withColor(14930457), GalacticraftMaterialAssetGroup.CHEESE);
        register(cxt, DESH, style -> style.withColor(2039582), GalacticraftMaterialAssetGroup.DESH);
        register(cxt, TITANIUM, style -> style.withColor(4014415), GalacticraftMaterialAssetGroup.TITANIUM);
        register(cxt, LEAD, style -> style.withColor(2764613), GalacticraftMaterialAssetGroup.LEAD);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, UnaryOperator<Style> style, MaterialAssetGroup assets) {
        Component component = Component.translatable(Util.makeDescriptionId("trim_material", key.identifier())).withStyle(style);
        context.register(key, new TrimMaterial(assets, component));
    }
}
