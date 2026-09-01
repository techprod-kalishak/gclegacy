/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.gear.FrequencyModuleModel;
import io.kalishak.galacticraftlegacy.client.renderer.entity.ObjModelHelper;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class FrequencyModuleLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.id("textures/model/frequency_module.png");

    public FrequencyModuleLayer(RenderLayerParent<S, M> renderer, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, null, layerRenderer, equipmentAssetManager);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        ItemStack stack = extractFromRenderState(renderState, GearRenderState.TELEMETRY_MODULE, GearRenderState::telemetryModule);
        boolean hasHelmet = renderState instanceof HumanoidRenderState humanoidRenderState && !humanoidRenderState.headEquipment.isEmpty();
        boolean isCrouching = renderState instanceof HumanoidRenderState humanoidRenderState && humanoidRenderState.isCrouching;
        QuadCollection baseQuads = Minecraft.getInstance().getModelManager().getStandaloneModel(FrequencyModuleModel.BASE_MODEL_KEY);
        QuadCollection radarQuads = Minecraft.getInstance().getModelManager().getStandaloneModel(FrequencyModuleModel.RADAR_MODEL_KEY);

        if (!stack.isEmpty() && baseQuads != null) {
            poseStack.pushPose();

            poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(renderState.xRot + 180.0F));
            poseStack.scale(0.3F, 0.3F, 0.3F);

            if (hasHelmet) {
                poseStack.translate(-1.1F, isCrouching ? 0.35F : 1.2F, 0.0F);
            } else {
                poseStack.translate(-0.9F, isCrouching ? 0.1F : 0.9F, 0.0F);
            }

            RenderType renderType = RenderTypes.entitySolid(TEXTURES);
            ObjModelHelper.renderQuads(baseQuads, poseStack, nodeCollector, renderType, quadInstance -> quadInstance.setLightCoords(renderState.lightCoords));
            poseStack.translate(0.0F, 1.3F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(renderState.ageInTicks * 0.05F) * 50.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(renderState.ageInTicks * 0.1F) * 50.0F));
            ObjModelHelper.renderQuads(radarQuads, poseStack, nodeCollector, renderType, quadInstance -> quadInstance.setLightCoords(renderState.lightCoords));

            poseStack.popPose();
        }
    }
}
