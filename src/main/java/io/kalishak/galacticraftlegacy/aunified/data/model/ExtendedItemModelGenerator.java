package io.kalishak.galacticraftlegacy.aunified.data.model;

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
import java.util.Collection;
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
    protected final List<TrimMaterialData> trimMaterials = new LinkedList<>();

    public ExtendedItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
        this.trimMaterials.addAll(VANILLA_TRIMS);
    }

    public void addTrims(Collection<TrimMaterialData> trims) {
        this.trimMaterials.addAll(trims);
    }

    @Override
    public void generateTrimmableItem(Item armor, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(armor);
        Material itemTexture = TextureMapping.getItemTexture(armor);
        Material overlayTexture = TextureMapping.getItemTexture(armor, "_overlay");

        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> cases = new ArrayList<>(this.trimMaterials.size());

        for (TrimMaterialData material : this.trimMaterials) {
            Identifier trimModelLocation = modelLocation.withSuffix("_" + material.assets().base().suffix() + "_trim");
            Material trimOverlayTexture = new Material(slotTrimPrefix.withSuffix("_" + material.assets().assetId(equipmentAssetId).suffix()));
            ItemModel.Unbaked trimModel;

            if (hasDyedLayer) {
                generateLayeredItem(trimModelLocation, itemTexture, overlayTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.tintedModel(trimModelLocation, new Dye(DyedItemColor.LEATHER_COLOR));
            } else {
                generateLayeredItem(trimModelLocation, itemTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.plainModel(trimModelLocation);
            }

            cases.add(ItemModelUtils.when(material.materialKey, trimModel));
        }

        ItemModel.Unbaked untrimmedModel;

        if (hasDyedLayer) {
            ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(itemTexture, overlayTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.tintedModel(modelLocation, new Dye(DyedItemColor.LEATHER_COLOR));
        } else {
            ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.plainModel(modelLocation);
        }

        this.itemModelOutput.accept(armor, ItemModelUtils.select(new TrimMaterialProperty(), untrimmedModel, cases));
    }

    public record TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) {

    }
}
