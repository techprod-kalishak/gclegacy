/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.aunified.data.model;

import com.google.common.collect.Lists;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.List;
import java.util.function.BiConsumer;

public class ExtendedItemModelGenerator extends ItemModelGenerators {
    protected static final List<TrimMaterialData> VANILLA_TRIMS = List.of(
            new TrimMaterialData(TrimMaterials.Palette.QUARTZ, TrimMaterials.QUARTZ),
            new TrimMaterialData(TrimMaterials.Palette.IRON, TrimMaterials.IRON),
            new TrimMaterialData(TrimMaterials.Palette.NETHERITE, TrimMaterials.NETHERITE),
            new TrimMaterialData(TrimMaterials.Palette.REDSTONE, TrimMaterials.REDSTONE),
            new TrimMaterialData(TrimMaterials.Palette.COPPER, TrimMaterials.COPPER),
            new TrimMaterialData(TrimMaterials.Palette.GOLD, TrimMaterials.GOLD),
            new TrimMaterialData(TrimMaterials.Palette.EMERALD, TrimMaterials.EMERALD),
            new TrimMaterialData(TrimMaterials.Palette.DIAMOND, TrimMaterials.DIAMOND),
            new TrimMaterialData(TrimMaterials.Palette.LAPIS, TrimMaterials.LAPIS),
            new TrimMaterialData(TrimMaterials.Palette.AMETHYST, TrimMaterials.AMETHYST),
            new TrimMaterialData(TrimMaterials.Palette.RESIN, TrimMaterials.RESIN)
    );
    protected final List<TrimMaterialData> trimMaterials = Lists.newArrayList(VANILLA_TRIMS);

    public ExtendedItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
        this.trimMaterials.addAll(getExtraTrims());
    }

    public List<TrimMaterialData> getExtraTrims() {
        return List.of();
    }

    public record TrimMaterialData(TrimMaterials.Palette assets, ResourceKey<TrimMaterial> materialKey) {

    }
}
