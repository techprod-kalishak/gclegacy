/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.block.SolarPanelModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.SolarPanelBlockRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractSolarPanelBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SolarPanelBlockRenderer<T extends AbstractSolarPanelBlockEntity> implements BlockEntityRenderer<T, SolarPanelBlockRenderState> {
    private static final Identifier BASIC_TEXTURES = Constants.id("textures/entity/solar_panel/basic.png");
    private static final Identifier ADVANCED_TEXTURES = Constants.id("textures/entity/solar_panel/advanced.png");

    private final SolarPanelModel model;

    public SolarPanelBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SolarPanelModel(context.bakeLayer(GalacticraftModelLayers.SOLAR_PANEL));
    }

    @Override
    public SolarPanelBlockRenderState createRenderState() {
        return new SolarPanelBlockRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, SolarPanelBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.lightCoords = blockEntity.getLevel() != null ? LightCoordsUtil.getLightCoords(blockEntity.getLevel(), blockEntity.getBlockPos().above()) : 15728880;
        state.angle = 6.0F;
    }

    @Override
    public void submit(SolarPanelBlockRenderState reusedState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.rotateDegrees(Axis.XP, 180.0F);
        poseStack.translate(0.5D, -2.5D, -0.5D);

        submitNodeCollector.submitModel(this.model, reusedState.angle, poseStack, getTexture(reusedState), reusedState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }

    @Override
    public @NonNull AABB getRenderBoundingBox(T blockEntity) {
        return new AABB(blockEntity.getBlockPos()).expandTowards(0.5D, 2.5D, 0.5D);
    }

    private Identifier getTexture(SolarPanelBlockRenderState state) {
        if (state.blockEntityType == GalacticraftBlockEntityTypes.ADVANCED_SOLAR_PANEL.get()) {
            return ADVANCED_TEXTURES;
        }

        return BASIC_TEXTURES;
    }
}
