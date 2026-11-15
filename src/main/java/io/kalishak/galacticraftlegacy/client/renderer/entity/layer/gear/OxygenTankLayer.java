/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.client.model.gear.GearEquipmentModel;
import io.kalishak.galacticraftlegacy.client.model.gear.OxygenTankModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * This can be extended of additional expansion tanks.
 */
public class OxygenTankLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    protected final GearEquipmentModel<S> mediumTank;
    protected final GearEquipmentModel<S> lightTank;

    public OxygenTankLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets, float yOffset) {
        super(renderer, new OxygenTankModel<>(modelSet.bakeLayer(GalacticraftModelLayers.HEAVY_OXYGEN_TANK)), layerRenderer, equipmentAssets, yOffset);
        this.mediumTank = new OxygenTankModel<>(modelSet.bakeLayer(GalacticraftModelLayers.MEDIUM_OXYGEN_TANK));
        this.lightTank = new OxygenTankModel<>(modelSet.bakeLayer(GalacticraftModelLayers.LIGHT_OXYGEN_TANK));
    }

    public OxygenTankLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets) {
        this(renderer, modelSet, layerRenderer, equipmentAssets, 0.2F);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        ItemStack leftTankStack = extractFromRenderState(renderState, GearRenderState.TANK, GearRenders::leftTank);
        ItemStack rightTankStack = extractFromRenderState(renderState, GearRenderState.ADDITIONAL_TANK, GearRenders::rightTank);

        GearEquippable.extractAssetId(leftTankStack).ifPresent(asset -> {
            Identifier textures = getGearTextures(asset, EnumExtensions.LAYER_TYPE_TANK.getValue());
            Model<S> leftTank = chooseTankModel(leftTankStack);

            poseStack.pushPose();
            transformTanks(poseStack, true);
            nodeCollector.submitModel(leftTank, renderState, poseStack, leftTank.renderType(textures), packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor);
            poseStack.popPose();
        });

        GearEquippable.extractAssetId(rightTankStack).ifPresent(asset -> {
            Identifier textures = getGearTextures(asset, EnumExtensions.LAYER_TYPE_TANK.getValue());
            Model<S> rightTank = chooseTankModel(rightTankStack);

            poseStack.pushPose();
            transformTanks(poseStack, false);
            nodeCollector.submitModel(rightTank, renderState, poseStack, rightTank.renderType(textures), packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor);
            poseStack.popPose();
        });
    }

    protected void transformTanks(PoseStack poseStack, boolean isLeftTank) {
        if (isLeftTank) {
            poseStack.translate(0.12F, this.yOffset, 0.2F);
        } else {
            poseStack.translate(-0.175F, this.yOffset, 0.2F);
        }
    }

    private Model<S> chooseTankModel(ItemStack stack) {
        Model<S> model = this.model;

        if (stack.is(GalacticraftItems.MEDIUM_TANK)) {
            model = this.mediumTank;
        } else if (stack.is(GalacticraftItems.LIGHT_TANK)) {
            model = this.lightTank;
        }

        return model;
    }
}
