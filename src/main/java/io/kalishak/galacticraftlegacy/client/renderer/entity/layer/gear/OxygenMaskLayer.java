/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.gear.GearEquipmentModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedEndermanRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class OxygenMaskLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.texture("entity/equipment/gear/oxygen_mask.png");

    public OxygenMaskLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets) {
        this(renderer, modelSet, layerRenderer, equipmentAssets, 0.02F);
    }

    public OxygenMaskLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets, float yOffset) {
        super(renderer, GearEquipmentModel.simple(modelSet.bakeLayer(GalacticraftModelLayers.OXYGEN_MASK), RenderTypes::entityCutout), layerRenderer, equipmentAssets, yOffset);
    }

    public static LayerDefinition createOxygenMaskLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.rotation(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        ItemStack stack = extractFromRenderState(renderState, GearRenderState.OXYGEN_MASK, GearRenders::oxygenMask);
        float newOffset = this.yOffset;

        if (renderState instanceof EvolvedEndermanRenderState endermanRenderState && endermanRenderState.isCreepy) {
            newOffset += 2.5F;
        }

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0F, newOffset, 0.0F);
            poseStack.scale(0.57F, 0.57F, 0.57F);
            nodeCollector.order(1).submitModel(this.model, renderState, poseStack, this.model.renderType(TEXTURES), packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor);
            poseStack.scale(1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}
