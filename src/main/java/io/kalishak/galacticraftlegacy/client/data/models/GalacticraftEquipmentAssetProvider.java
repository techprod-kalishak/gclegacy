/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data.models;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GalacticraftEquipmentAssetProvider extends EquipmentAssetProvider {
    public GalacticraftEquipmentAssetProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(
                GearEquipmentAssets.SENSOR_GLASSES,
                custom(GearEquipmentAssets.SENSOR_GLASSES, () -> EquipmentClientInfo.LayerType.HUMANOID)
        );
        output.accept(
                GearEquipmentAssets.STEEL,
                humanoidAndMounts(GearEquipmentAssets.STEEL)
        );
        output.accept(
                GearEquipmentAssets.DESH,
                humanoidOnly(GearEquipmentAssets.DESH)
        );
        output.accept(
                GearEquipmentAssets.TITANIUM,
                humanoidOnly(GearEquipmentAssets.TITANIUM)
        );

        output.accept(
                GearEquipmentAssets.THERMAL,
                thermalHumanoid(GearEquipmentAssets.THERMAL)
        );
        output.accept(
                GearEquipmentAssets.ISOTHERMAL,
                thermalHumanoid(GearEquipmentAssets.ISOTHERMAL)
        );
        output.accept(
                GearEquipmentAssets.WOLF_THERMAL,
                custom(GearEquipmentAssets.WOLF_THERMAL, () -> EquipmentClientInfo.LayerType.WOLF_BODY)
        );
        output.accept(
                GearEquipmentAssets.HEAVY_TANK,
                custom(GearEquipmentAssets.HEAVY_TANK, EnumExtensions.LAYER_TYPE_TANK::getValue)
        );
        output.accept(
                GearEquipmentAssets.MEDIUM_TANK,
                custom(GearEquipmentAssets.MEDIUM_TANK, EnumExtensions.LAYER_TYPE_TANK::getValue)
        );
        output.accept(
                GearEquipmentAssets.LIGHT_TANK,
                custom(GearEquipmentAssets.LIGHT_TANK, EnumExtensions.LAYER_TYPE_TANK::getValue)
        );

        for (Map.Entry<DyeColor, ResourceKey<EquipmentAsset>> entry : GearEquipmentAssets.PARACHUTES.entrySet()) {
            DyeColor dyeColor = entry.getKey();
            ResourceKey<EquipmentAsset> resourceKey = entry.getValue();
            output.accept(
                    resourceKey,
                    EquipmentClientInfo.builder()
                            .addLayers(
                                    EnumExtensions.LAYER_TYPE_PARACHUTE.getValue(),
                                    EquipmentClientInfo.Layer.onlyIfDyed(Constants.id(dyeColor.getSerializedName()), false)
                            )
                            .build()
            );
        }
    }

    protected static EquipmentClientInfo custom(ResourceKey<EquipmentAsset> assetKey, Supplier<EquipmentClientInfo.LayerType> layerType) {
        return EquipmentClientInfo.builder()
                .addLayers(layerType.get(), new EquipmentClientInfo.Layer(assetKey.identifier()))
                .build();
    }

    protected static EquipmentClientInfo thermalHumanoid(ResourceKey<EquipmentAsset> assetKey) {
        return EquipmentClientInfo.builder()
                .addLayers(EnumExtensions.LAYER_TYPE_THERMAL_PADDING.getValue(), new EquipmentClientInfo.Layer(assetKey.identifier()))
                .addLayers(EnumExtensions.LAYER_TYPE_THERMAL_PADDING_LEGGINGS.getValue(), new EquipmentClientInfo.Layer(assetKey.identifier()))
                .build();
    }

    protected static EquipmentClientInfo humanoidOnly(ResourceKey<EquipmentAsset> assetKey) {
        return EquipmentClientInfo.builder()
                .addHumanoidLayers(assetKey.identifier())
                .build();
    }

    protected static EquipmentClientInfo humanoidAndMounts(ResourceKey<EquipmentAsset> assetKey) {
        return EquipmentClientInfo.builder()
                .addHumanoidLayers(assetKey.identifier())
                .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(assetKey.identifier(), false))
                .addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, EquipmentClientInfo.Layer.leatherDyeable(assetKey.identifier(), false))
                .build();
    }
}
