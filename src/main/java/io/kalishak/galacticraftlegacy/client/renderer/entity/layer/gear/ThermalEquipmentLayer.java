/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ThermalEquipmentLayer<S extends LivingEntityRenderState, M extends Model<S>> {
    Identifier PADDING_LAYER = Constants.texture("entity/equipment/galacticraftlegacy/thermal_padding/colored_layer.png");
    Identifier PADDING_LEGGINGS_LAYER = Constants.texture("entity/equipment/galacticraftlegacy/thermal_padding_leggings/colored_layer.png");

    M getModel(EquipmentSlot slot, S renderState);

    EquipmentAssetManager getEquipmentAssetManager();

    default void renderThermalPiece(PoseStack poseStack, SubmitNodeCollector nodeCollector, ItemStack item, GearEquipmentSlot slot, int packedLight, S renderState, EquipmentClientInfo.LayerType layerType, Identifier paddingLayer) {
        GearEquippable gearEquippable = item.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);
        if (gearEquippable != null && ThermalEquipmentLayer.shouldRender(gearEquippable, slot, renderState.isInvisible)) {
            M model = getModel(slot.getRelatedEquipment(), renderState);
            int color = getColor(renderState);
            List<EquipmentClientInfo.Layer> list = getEquipmentAssetManager().get(gearEquippable.assetId().orElseThrow()).getLayers(layerType);

            if (!list.isEmpty()) {
                EquipmentClientInfo.Layer layer = list.getFirst();
                nodeCollector
                        .order(1)
                        .submitModel(
                                model,
                                renderState,
                                poseStack,
                                RenderTypes.armorCutoutNoCull(layer.getTextureLocation(layerType)),
                                packedLight,
                                OverlayTexture.NO_OVERLAY,
                                renderState.outlineColor,
                                null
                        );
                nodeCollector
                        .order(2)
                        .submitModel(
                                model,
                                renderState,
                                poseStack,
                                RenderTypes.armorTranslucent(paddingLayer),
                                packedLight,
                                OverlayTexture.NO_OVERLAY,
                                color,
                                null,
                                renderState.outlineColor,
                                null
                        );
            }
        }
    }

    default void renderThermalPiece(PoseStack poseStack, SubmitNodeCollector nodeCollector, ItemStack item, GearEquipmentSlot slot, int packedLight, S renderState) {
        renderThermalPiece(poseStack, nodeCollector, item, slot, packedLight, renderState, EnumExtensions.LAYER_TYPE_THERMAL_PADDING.getValue(), PADDING_LAYER);
    }

    static boolean shouldRender(ItemStack stack, GearEquipmentSlot gearSlot, boolean invisible) {
        GearEquippable gearEquippable = stack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);
        return gearEquippable != null && shouldRender(gearEquippable, gearSlot, invisible);
    }

    static boolean shouldRender(GearEquippable gearEquippable, GearEquipmentSlot gearSlot, boolean invisible) {
        if (invisible) {
            return false;
        }

        return gearEquippable.assetId().isPresent() && gearEquippable.gearSlot() == gearSlot;
    }

    private static <S extends LivingEntityRenderState> int getColor(S renderState) {
        float time = renderState.ageInTicks / 10.0F;
        float sTime = (float) Math.sin(time) * 0.5F + 0.5F;

        float r = 0.2F * sTime;
        float g = sTime;
        float b = 0.2F * sTime;

        float temperatureModifier = renderState.getRenderDataOrDefault(GearRenderState.TEMPERATURE_MODIFIER, 0.0F);

        if (temperatureModifier > 0.0F) {
            b = g;
            g = r;
        } else if (temperatureModifier < 0.0F) {
            r = g;
            g = b;
        }

        int color = ARGB.colorFromFloat(0.4F * sTime, r, g, b);
        return ARGB.setBrightness(color, 1.0F);
    }
}
