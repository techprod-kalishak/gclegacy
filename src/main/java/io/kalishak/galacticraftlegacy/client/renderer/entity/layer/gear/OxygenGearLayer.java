/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.model.gear.OxygenGearModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;

public class OxygenGearLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.texture("entity/equipment/gear/oxygen_gear.png");

    public OxygenGearLayer(RenderLayerParent<S, M> renderer, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, OxygenGearModel::new, GalacticraftModelLayers.OXYGEN_GEAR, entityModels, layerRenderer, equipmentAssetManager);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        if (getDataFromContext(renderState, GearRenderState.HAS_OXYGEN_GEAR, false)) {

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 2; j++) {
                    poseStack.pushPose();
                    poseStack.translate(0.175F * (float) (j * 2 - 1), 0.0F, 0.0F);
                    poseStack.translate(0.0F, -0.0325F * (float) (i * 2 - 1), 0.0F);
                    poseStack.translate(0.0F, 0.0F, -0.0325F * (float) (Math.pow(i * 2 - 1, 2) * 0.05));
                    poseStack.translate(0.0F, 0.2F, 0.0F);
                    poseStack.translate(0.0F, 0.0F, 0.2F);

                    nodeCollector.submitModelPart(
                            ((OxygenGearModel<S>) this.model).tubes[j][i],
                            poseStack,
                            RenderTypes.entitySolid(TEXTURES),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            null
                    );

                    poseStack.popPose();
                }
            }
        }
    }
}
