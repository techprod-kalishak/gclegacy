/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.aunified.data.model;

import com.google.common.collect.Lists;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class ExtendedItemModelGenerator extends ItemModelGenerators {
    protected static final List<TrimMaterialData> VANILLA_TRIMS = List.of(
            new TrimMaterialData(MaterialAssetGroup.QUARTZ, TrimMaterials.QUARTZ),
            new TrimMaterialData(MaterialAssetGroup.IRON, TrimMaterials.IRON),
            new TrimMaterialData(MaterialAssetGroup.NETHERITE, TrimMaterials.NETHERITE),
            new TrimMaterialData(MaterialAssetGroup.REDSTONE, TrimMaterials.REDSTONE),
            new TrimMaterialData(MaterialAssetGroup.COPPER, TrimMaterials.COPPER),
            new TrimMaterialData(MaterialAssetGroup.GOLD, TrimMaterials.GOLD),
            new TrimMaterialData(MaterialAssetGroup.EMERALD, TrimMaterials.EMERALD),
            new TrimMaterialData(MaterialAssetGroup.DIAMOND, TrimMaterials.DIAMOND),
            new TrimMaterialData(MaterialAssetGroup.LAPIS, TrimMaterials.LAPIS),
            new TrimMaterialData(MaterialAssetGroup.AMETHYST, TrimMaterials.AMETHYST),
            new TrimMaterialData(MaterialAssetGroup.RESIN, TrimMaterials.RESIN)
    );
    protected final List<TrimMaterialData> trimMaterials = Lists.newArrayList(VANILLA_TRIMS);

    public ExtendedItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
        this.trimMaterials.addAll(getExtraTrims());
    }

    public List<TrimMaterialData> getExtraTrims() {
        return List.of();
    }

    public record TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) {

    }
}
