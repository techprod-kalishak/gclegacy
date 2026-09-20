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
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ExtendedItemModelGenerator extends ItemModelGenerators {
    protected static final List<TrimMaterialData> VANILLA_TRIMS = List.of(
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.QUARTZ), TrimMaterials.QUARTZ),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.IRON), TrimMaterials.IRON),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.NETHERITE), TrimMaterials.NETHERITE),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.REDSTONE), TrimMaterials.REDSTONE),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.COPPER), TrimMaterials.COPPER),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.GOLD), TrimMaterials.GOLD),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.EMERALD), TrimMaterials.EMERALD),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.DIAMOND), TrimMaterials.DIAMOND),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.LAPIS), TrimMaterials.LAPIS),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.AMETHYST), TrimMaterials.AMETHYST),
            new TrimMaterialData(TrimPalleteGetter.fromVanilla(TrimMaterials.Palette.RESIN), TrimMaterials.RESIN)
    );
    protected final List<TrimMaterialData> trimMaterials = Lists.newArrayList(VANILLA_TRIMS);

    public ExtendedItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
        this.trimMaterials.addAll(getExtraTrims());
    }

    public List<TrimMaterialData> getExtraTrims() {
        return List.of();
    }

    public void generateCustomTrimmableArmorSet(Item helmet, Item chestplate, Item leggings, Item boots, boolean hasDyedLayer, Map<TrimPalleteGetter, TrimPalleteGetter> trimPaletteReplacements) {
        generateCustomTrimmableItem(helmet, TRIM_PREFIX_HELMET, hasDyedLayer, trimPaletteReplacements);
        generateCustomTrimmableItem(chestplate, TRIM_PREFIX_CHESTPLATE, hasDyedLayer, trimPaletteReplacements);
        generateCustomTrimmableItem(leggings, TRIM_PREFIX_LEGGINGS, hasDyedLayer, trimPaletteReplacements);
        generateCustomTrimmableItem(boots, TRIM_PREFIX_BOOTS, hasDyedLayer, trimPaletteReplacements);
    }

    public void generateCustomTrimmableItem(Item armor, Identifier slotTrimPrefix, boolean hasDyedLayer, Map<TrimPalleteGetter, TrimPalleteGetter> trimPaletteReplacements) {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(armor);
        Material itemTexture = TextureMapping.getItemTexture(armor);
        Material overlayTexture = TextureMapping.getItemTexture(armor, "_overlay");
        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> cases = new ArrayList<>(VANILLA_TRIMS.size());

        for (ExtendedItemModelGenerator.TrimMaterialData material : VANILLA_TRIMS) {
            Identifier trimModelLocation = modelLocation.withSuffix("_" + material.palette().suffix() + "_trim");
            TrimPalleteGetter palette = trimPaletteReplacements.getOrDefault(material.palette(), material.palette());
            Material trimOverlayTexture = new Material(slotTrimPrefix.withSuffix("_" + palette.suffix()));
            ItemModel.Unbaked trimModel;
            if (hasDyedLayer) {
                this.generateLayeredItem(trimModelLocation, itemTexture, overlayTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.tintedModel(trimModelLocation, new Dye(-6265536));
            } else {
                this.generateLayeredItem(trimModelLocation, itemTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.plainModel(trimModelLocation);
            }

            cases.add(ItemModelUtils.when(material.materialKey, trimModel));
        }

        ItemModel.Unbaked untrimmedModel;
        if (hasDyedLayer) {
            ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(itemTexture, overlayTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.tintedModel(modelLocation, new Dye(-6265536));
        } else {
            ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.plainModel(modelLocation);
        }

        this.itemModelOutput.accept(armor, ItemModelUtils.select(new TrimMaterialProperty(), untrimmedModel, cases));
    }

    public record TrimMaterialData(TrimPalleteGetter palette, ResourceKey<TrimMaterial> materialKey) {

    }

    public interface TrimPalleteGetter {
        String suffix();
        Identifier id();

        static TrimPalleteGetter fromVanilla(TrimMaterials.Palette palette) {
            return new TrimPalleteGetter() {
                @Override
                public String suffix() {
                    return palette.suffix();
                }

                @Override
                public Identifier id() {
                    return palette.id();
                }
            };
        }
    }
}
